package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetFavoritesUseCaseImplTest {

    private val repository: FavoriteCharacterRepository = mockk()
    private val useCase = GetFavoritesUseCaseImpl(repository)

    @Test
    fun `execute should return the list of favorite characters from repository`() {
        // Arrange
        val mockFavorites = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty")
        )
        every { repository.getFavorites() } returns mockFavorites

        // Act
        val result = useCase.execute()

        // Assert
        assertEquals(mockFavorites, result)
    }

    @Test
    fun `execute should return an empty list when there are no favorites`() {
        // Arrange
        every { repository.getFavorites() } returns emptyList()

        // Act
        val result = useCase.execute()

        // Assert
        assertEquals(emptyList<Character>(), result)
    }
}