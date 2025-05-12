package com.example.rickandmorty.features.character.data

import com.example.rickandmorty.features.character.data.datasource.FavoriteCharacterLocalDataSource
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class FavoriteCharacterRepositoryImplTest {

    private val localDataSource: FavoriteCharacterLocalDataSource = mockk()
    private val repository = FavoriteCharacterRepositoryImpl(localDataSource)

    @Test
    fun `saveFavorite should call localDataSource saveFavorite`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        justRun { localDataSource.saveFavorite(character) }

        // Act
        repository.saveFavorite(character)

        // Assert
        verify { localDataSource.saveFavorite(character) }
    }

    @Test
    fun `removeFavorite should call localDataSource removeFavorite`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        justRun { localDataSource.removeFavorite(character) }

        // Act
        repository.removeFavorite(character)

        // Assert
        verify { localDataSource.removeFavorite(character) }
    }

    @Test
    fun `getFavorites should return the list of favorite characters from localDataSource`() {
        // Arrange
        val mockFavorites = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty")
        )
        every { localDataSource.getFavorites() } returns mockFavorites

        // Act
        val result = repository.getFavorites()

        // Assert
        assertEquals(mockFavorites, result)
    }

    @Test
    fun `isFavorite should return true if character is in favorites`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        val mockFavorites = listOf(character)
        every { localDataSource.getFavorites() } returns mockFavorites

        // Act
        val result = repository.isFavorite(character)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isFavorite should return false if character is not in favorites`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        val mockFavorites = emptyList<Character>()
        every { localDataSource.getFavorites() } returns mockFavorites

        // Act
        val result = repository.isFavorite(character)

        // Assert
        assertFalse(result)
    }
}