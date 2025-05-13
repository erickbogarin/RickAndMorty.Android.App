package com.example.rickandmorty.features.character.data.datasource

import com.example.rickandmorty.commons.serialization.JsonSerializer
import com.example.rickandmorty.commons.storage.LocalStorage
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FavoriteCharacterLocalDataSourceTest {

    private val localStorage: LocalStorage = mockk()
    private val jsonSerializer: JsonSerializer = mockk()
    private val dataSource = FavoriteCharacterLocalDataSource(localStorage, jsonSerializer)

    @Test
    fun `saveFavorite should add character to favorites and save to SharedPreferences`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        val existingFavorites = listOf(createMockCharacter(id = 2, name = "Morty"))
        val updatedFavorites = existingFavorites + character
        val json = "mocked_json"

        // Mock para a chamada localStorage.getString
        every { localStorage.getString("favorite_characters", "[]") } returns json

        val type = mockk<java.lang.reflect.Type>()
        every { jsonSerializer.listType(Character::class.java) } returns type
        every { jsonSerializer.fromJson<List<Character>>(eq(json), eq(type)) } returns existingFavorites
        every { jsonSerializer.toJson(updatedFavorites) } returns "updated_json"
        justRun { localStorage.putString("favorite_characters", "updated_json") }

        // Act
        dataSource.saveFavorite(character)

        // Assert
        verify { localStorage.putString("favorite_characters", "updated_json") }
    }

    @Test
    fun `removeFavorite should remove character from favorites and save to SharedPreferences`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        val existingFavorites = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty"),
        )
        val updatedFavorites = listOf(createMockCharacter(id = 2, name = "Morty"))
        val json = "mocked_json"

        // Mock para a chamada localStorage.getString
        every { localStorage.getString("favorite_characters", "[]") } returns json

        val type = mockk<java.lang.reflect.Type>()
        every { jsonSerializer.listType(Character::class.java) } returns type
        every { jsonSerializer.fromJson<List<Character>>(eq(json), eq(type)) } returns existingFavorites
        every { jsonSerializer.toJson(updatedFavorites) } returns "updated_json"
        justRun { localStorage.putString("favorite_characters", "updated_json") }

        // Act
        dataSource.removeFavorite(character)

        // Assert
        verify { localStorage.putString("favorite_characters", "updated_json") }
    }

    @Test
    fun `getFavorites should return list of favorite characters from SharedPreferences`() {
        // Arrange
        val mockFavorites = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty"),
        )
        val json = "mocked_json"

        // Mock para a chamada localStorage.getString
        every { localStorage.getString("favorite_characters", "[]") } returns json

        val type = mockk<java.lang.reflect.Type>()
        every { jsonSerializer.listType(Character::class.java) } returns type
        every { jsonSerializer.fromJson<List<Character>>(eq(json), eq(type)) } returns mockFavorites

        // Act
        val result = dataSource.getFavorites()

        // Assert
        assertEquals(mockFavorites, result)
    }
}
