package com.example.rickandmorty.features.character.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.commons.base_ui.BaseViewModel
import com.example.rickandmorty.commons.utils.pagination.PaginationCallback
import com.example.rickandmorty.commons.utils.pagination.PaginationState
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.episodes.data.repository.EndOfListException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : BaseViewModel(), PaginationCallback {

    val characters = MutableLiveData<List<Character>>()

    val paginationState = PaginationState()

    val currentPage: LiveData<Int> get() = paginationState.currentPage
    val isLastPage: LiveData<Boolean> get() = paginationState.isLastPage
    val endOfList: LiveData<Boolean> get() = paginationState.endOfList

    @SuppressLint("CheckResult")
    override fun onLoadMore() {
        getCharacters()
    }

    @SuppressLint("CheckResult")
    fun getCharacters() {
        if (paginationState.isLastPage.value == true) return

        getCharactersUseCase.execute(paginationState.currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newCharacters ->
                    characters.value = characters.value.orEmpty() + newCharacters
                },
                { error ->
                    if (error is EndOfListException) {
                        paginationState.markAsLastPage()
                    } else {
                        Log.e("Episodes", error.message.orEmpty())
                    }
                }
            )
    }
}