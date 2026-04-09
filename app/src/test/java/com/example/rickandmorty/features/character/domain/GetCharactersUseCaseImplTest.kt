package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.commons.pagination.PaginatedResult
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
    fun `execute should return paginated characters when repository call is successful`() {
        val mockCharacters = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty"),
        )
        val paginatedResult = PaginatedResult(mockCharacters, hasNextPage = true)
        every { repository.getAllCharacters(1) } returns Single.just(paginatedResult)

        val result = useCase.execute(1).blockingGet()

        assertEquals(paginatedResult, result)
    }

    @Test
    fun `execute should throw an exception when repository call fails`() {
        val errorMessage = "Network error"
        every { repository.getAllCharacters(1) } returns Single.error(RuntimeException(errorMessage))

        val exception = assertThrows(RuntimeException::class.java) {
            useCase.execute(1).blockingGet()
        }
        assertEquals(errorMessage, exception.message)
    }
}
