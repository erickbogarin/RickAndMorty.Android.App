package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.features.character.data.model.Character
import javax.inject.Inject

class CheckFavoriteCharacterUseCase @Inject constructor(
    private val repository: FavoriteCharacterRepository
) {
    fun execute(character: Character): Boolean {
        return repository.isFavorite(character)
    }
}