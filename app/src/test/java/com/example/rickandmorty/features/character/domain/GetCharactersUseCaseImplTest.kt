package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.CharacterRepository
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class GetCharactersUseCaseImplTest {

    private val repository: CharacterRepository = mockk()
    private val useCase = GetCharactersUseCaseImpl(repository)

    @Test
    fun `execute should return a list of characters when repository call is successful`() {
        // Arrange
        val mockCharacters = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty")
        )
        every { repository.getAllCharacters(1) } returns Single.just(mockCharacters)

        // Act
        val result = useCase.execute(1).blockingGet()

        // Assert
        assertEquals(mockCharacters, result)
    }

    @Test
    fun `execute should throw an exception when repository call fails`() {
        // Arrange
        val errorMessage = "Network error"
        every { repository.getAllCharacters(1) } returns Single.error(RuntimeException(errorMessage))

        // Act & Assert
        val exception = assertThrows(RuntimeException::class.java) {
            useCase.execute(1).blockingGet()
        }
        assertEquals(errorMessage, exception.message)
    }
}