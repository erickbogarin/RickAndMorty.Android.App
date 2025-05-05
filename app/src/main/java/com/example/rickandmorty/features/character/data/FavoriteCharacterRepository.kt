package com.example.rickandmorty.features.character.data

import android.content.SharedPreferences
import com.example.rickandmorty.features.character.data.model.Character
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

interface FavoriteCharacterRepository {
    fun saveFavorite(character: Character)
    fun removeFavorite(character: Character)
    fun getFavorites(): List<Character>
    fun isFavorite(character: Character): Boolean
}

class FavoriteCharacterRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : FavoriteCharacterRepository {

    private val key = "favorite_characters"

    override fun saveFavorite(character: Character) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.contains(character)) {
            favorites.add(character)
            saveToPreferences(favorites)
        }
    }

    override fun removeFavorite(character: Character) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(character)
        saveToPreferences(favorites)
    }

    override fun getFavorites(): List<Character> {
        val json = sharedPreferences.getString(key, "[]")
        val type = object : TypeToken<List<Character>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    override fun isFavorite(character: Character): Boolean {
        return getFavorites().contains(character)
    }

    private fun saveToPreferences(favorites: List<Character>) {
        sharedPreferences.edit().putString(key, gson.toJson(favorites)).apply()
    }
}