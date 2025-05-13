package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CheckFavoriteCharacterUseCaseTest {

    private val repository: FavoriteCharacterRepository = mockk()
    private val useCase = CheckFavoriteCharacterUseCase(repository)

    @Test
    fun `execute should return true when character is favorite`() {
        // Arrange
        val character = createMockCharacter(id = 1, name = "Rick")
        every { repository.isFavorite(character) } returns true

        // Act
        val result = useCase.execute(character)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `execute should return false when character is not favorite`() {
        // Arrange
        val character = createMockCharacter(id = 2, name = "Morty")
        every { repository.isFavorite(character) } returns false

        // Act
        val result = useCase.execute(character)

        // Assert
        assertFalse(result)
    }
}
