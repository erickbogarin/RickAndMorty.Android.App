package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.features.character.data.model.Character
import javax.inject.Inject

class ToggleFavoriteCharacterUseCase @Inject constructor(
    private val repository: FavoriteCharacterRepository
) {
    fun execute(character: Character): Boolean {
        return if (repository.isFavorite(character)) {
            repository.removeFavorite(character)
            false
        } else {
            repository.saveFavorite(character)
            true
        }
    }
}