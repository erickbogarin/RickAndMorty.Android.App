package com.example.rickandmorty.features.character.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.commons.baseui.BaseViewModel
import com.example.rickandmorty.commons.utils.pagination.PaginationCallback
import com.example.rickandmorty.commons.utils.pagination.PaginationState
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.domain.CheckFavoriteCharacterUseCase
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.ToggleFavoriteCharacterUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

data class CharacterCardUiModel(
    val character: Character,
    val isFavorite: Boolean,
)

data class CharacterUiState(
    val items: List<CharacterCardUiModel> = emptyList(),
    val showOnlyFavorites: Boolean = false,
    val showEmptyFavoritesMessage: Boolean = false,
)

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase,
    private val checkFavoriteCharacterUseCase: CheckFavoriteCharacterUseCase,
) : BaseViewModel(), PaginationCallback {

    private val _allCharacters = MutableLiveData<List<Character>>(emptyList())

    private val _showOnlyFavorites = MutableLiveData<Boolean>(false)
    val showOnlyFavorites: LiveData<Boolean> = _showOnlyFavorites

    private val _uiState = MutableLiveData(CharacterUiState())
    val uiState: LiveData<CharacterUiState> = _uiState

    val paginationState = PaginationState()

    @SuppressLint("CheckResult")
    override fun onLoadMore() {
        if (_showOnlyFavorites.value == true ||
            paginationState.isLastPage.value == true ||
            paginationState.isLoading.value == true
        ) {
            return
        }

        paginationState.startLoading()
        compositeDisposable + getCharactersUseCase.execute(paginationState.currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    paginationState.finishLoading()
                    _allCharacters.value = _allCharacters.value.orEmpty() + result.items
                    publishUiState()
                    if (result.hasNextPage) {
                        paginationState.advancePage()
                    } else {
                        paginationState.markAsLastPage()
                    }
                },
                { error ->
                    paginationState.finishLoading()
                    Log.e("CharacterViewModel", error.message.orEmpty())
                },
            )
    }

    fun toggleFavorite(character: Character) {
        toggleFavoriteCharacterUseCase.execute(character)
        publishUiState()
    }

    fun setShowOnlyFavorites(showOnlyFavorites: Boolean) {
        _showOnlyFavorites.value = showOnlyFavorites
        publishUiState()
    }

    private fun publishUiState() {
        val showOnlyFavorites = _showOnlyFavorites.value == true
        val items = _allCharacters.value.orEmpty()
            .map { character ->
                CharacterCardUiModel(
                    character = character,
                    isFavorite = checkFavoriteCharacterUseCase.execute(character),
                )
            }
            .let { characters ->
                if (showOnlyFavorites) {
                    characters.filter { it.isFavorite }
                } else {
                    characters
                }
            }

        _uiState.value = CharacterUiState(
            items = items,
            showOnlyFavorites = showOnlyFavorites,
            showEmptyFavoritesMessage = showOnlyFavorites && items.isEmpty(),
        )
    }
}
