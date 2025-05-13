package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.features.character.data.model.Character
import javax.inject.Inject

interface GetFavoritesUseCase {
    fun execute(): List<Character>
}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val repository: FavoriteCharacterRepository,
) : GetFavoritesUseCase {
    override fun execute(): List<Character> = repository.getFavorites()
}
