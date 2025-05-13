package com.example.rickandmorty.features.character.data

import com.example.rickandmorty.features.character.data.datasource.FavoriteCharacterLocalDataSource
import com.example.rickandmorty.features.character.data.model.Character
import javax.inject.Inject

interface FavoriteCharacterRepository {
    fun saveFavorite(character: Character)
    fun removeFavorite(character: Character)
    fun getFavorites(): List<Character>
    fun isFavorite(character: Character): Boolean
}

class FavoriteCharacterRepositoryImpl @Inject constructor(
    private val localDataSource: FavoriteCharacterLocalDataSource,
) : FavoriteCharacterRepository {

    override fun saveFavorite(character: Character) {
        localDataSource.saveFavorite(character)
    }

    override fun removeFavorite(character: Character) {
        localDataSource.removeFavorite(character)
    }

    override fun getFavorites(): List<Character> {
        return localDataSource.getFavorites()
    }

    override fun isFavorite(character: Character): Boolean {
        val favorites = getFavorites()
        return character.id?.let { characterId ->
            favorites?.any { it.id == characterId }
        } ?: false
    }
}
