package com.example.rickandmorty.features.character.data.datasource

import android.content.SharedPreferences
import com.example.rickandmorty.features.character.data.model.Character
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class FavoriteCharacterLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
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
        val json = sharedPreferences.getString(key, "[]")
        val type = object : TypeToken<List<Character>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun saveToPreferences(favorites: List<Character>) {
        sharedPreferences.edit().putString(key, gson.toJson(favorites)).apply()
    }
}