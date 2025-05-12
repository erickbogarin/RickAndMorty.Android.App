package com.example.rickandmorty.features.character.data.datasource

import com.example.rickandmorty.commons.serialization.JsonSerializer
import com.example.rickandmorty.commons.storage.LocalStorage
import com.example.rickandmorty.features.character.data.model.Character
import javax.inject.Inject

class FavoriteCharacterLocalDataSource @Inject constructor(
    private val localStorage: LocalStorage,
    private val jsonSerializer: JsonSerializer
) {

    private val key = "favorite_characters"

    fun saveFavorite(character: Character) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.contains(character)) {
            favorites.add(character)
            saveToPreferences(favorites)
        }
    }

    fun removeFavorite(character: Character) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(character)
        saveToPreferences(favorites)
    }

    fun getFavorites(): List<Character> {
        val json = localStorage.getString(key, "[]") ?: "[]"
        val type = jsonSerializer.listType(Character::class.java)
        return jsonSerializer.fromJson(json, type) ?: emptyList()
    }

    private fun saveToPreferences(favorites: List<Character>) {
        localStorage.putString(key, jsonSerializer.toJson(favorites))
    }
}