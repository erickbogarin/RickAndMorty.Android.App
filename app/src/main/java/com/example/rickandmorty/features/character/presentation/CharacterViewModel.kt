package com.example.rickandmorty.features.character.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.rickandmorty.commons.baseui.BaseViewModel
import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.utils.pagination.PaginationCallback
import com.example.rickandmorty.commons.utils.pagination.PaginationState
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.domain.CheckFavoriteCharacterUseCase
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.ToggleFavoriteCharacterUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase,
    private val checkFavoriteCharacterUseCase: CheckFavoriteCharacterUseCase,
) : BaseViewModel(), PaginationCallback {

    private val _allCharacters = MutableLiveData<List<Character>>(emptyList())

    private val _showOnlyFavorites = MutableLiveData<Boolean>(false)
    val showOnlyFavorites: LiveData<Boolean> = _showOnlyFavorites

    val characters: LiveData<List<Character>> = _allCharacters.switchMap { allCharacters ->
        _showOnlyFavorites.map { showOnlyFavorites ->
            if (showOnlyFavorites) {
                allCharacters.filter { isFavorite(it) }
            } else {
                allCharacters
            }
        }
    }

    val paginationState = PaginationState()
    val favoriteStatus = MutableLiveData<Pair<Character, Boolean>>()

    @SuppressLint("CheckResult")
    override fun onLoadMore() {
        if (_showOnlyFavorites.value == true || paginationState.isLastPage.value == true) return

        getCharactersUseCase.execute(paginationState.currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newCharacters ->
                    _allCharacters.value = _allCharacters.value.orEmpty() + newCharacters
                    paginationState.incrementCurrentPage()
                },
                { error ->
                    if (error is EndOfListException) {
                        paginationState.markAsLastPage()
                    } else {
                        Log.e("CharacterViewModel", error.message.orEmpty())
                    }
                },
            )
    }

    fun toggleFavorite(character: Character) {
        val isFavorite = toggleFavoriteCharacterUseCase.execute(character)
        favoriteStatus.value = character to isFavorite

        if (_showOnlyFavorites.value == true) {
            refreshCharacterList()
        }
    }

    fun isFavorite(character: Character): Boolean {
        return checkFavoriteCharacterUseCase.execute(character)
    }

    fun setShowOnlyFavorites(showOnlyFavorites: Boolean) {
        _showOnlyFavorites.value = showOnlyFavorites
    }

    private fun refreshCharacterList() {
        _allCharacters.value = _allCharacters.value
    }
}
