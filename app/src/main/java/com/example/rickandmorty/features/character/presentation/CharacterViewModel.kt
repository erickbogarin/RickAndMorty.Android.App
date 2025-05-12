package com.example.rickandmorty.features.character.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.base.BaseViewModel
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : BaseViewModel() {

    val characters = MutableLiveData<List<Character>>()
    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> get() = _currentPage

    fun incrementCurrentPage() {
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }

    @SuppressLint("CheckResult")
    fun getCharacters() {
        getCharactersUseCase.execute(_currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newCharacters ->
                    characters.value = characters.value.orEmpty() + newCharacters
                },
                { error ->
                    Log.e("Characters", error.message.orEmpty())
                }
            )
    }
}