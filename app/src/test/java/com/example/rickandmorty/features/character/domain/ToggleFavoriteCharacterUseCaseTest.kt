package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ToggleFavoriteCharacterUseCaseTest {

    private val repository: FavoriteCharacterRepository = mockk(relaxed = true)
    private val useCase = ToggleFavoriteCharacterUseCase(repository)

    @Test
    fun `execute should remove favorite and return false when character is already favorite`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        every { repository.isFavorite(character) } returns true

        // Act
        val result = useCase.execute(character)

        // Assert
        verify { repository.removeFavorite(character) }
        assertFalse(result)
    }

    @Test
    fun `execute should save favorite and return true when character is not favorite`() {
        // Arrange
        val character = createMockCharacter(id = 2, name = "Morty")
        every { repository.isFavorite(character) } returns false

        // Act
        val result = useCase.execute(character)

        // Assert
        verify { repository.saveFavorite(character) }
        assertTrue(result)
    }
}