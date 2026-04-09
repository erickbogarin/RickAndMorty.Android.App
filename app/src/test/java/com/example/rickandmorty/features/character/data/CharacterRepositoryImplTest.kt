package com.example.rickandmorty.features.character.data

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.character.data.model.CharacterResponse
import com.example.rickandmorty.utils.character.createMockCharacter
import com.example.rickandmorty.utils.character.createMockInfo
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import retrofit2.Response

class CharacterRepositoryImplTest {

    private val service: CharacterApiService = mockk()
    private val repository = CharacterRepositoryImpl(service)

    @Test
    fun `getAllCharacters should return paginated result when response is successful`() {
        val mockInfo = createMockInfo(next = "https://rickandmortyapi.com/api/character?page=2")
        val mockCharacters = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty"),
        )
        val mockResponse = Response.success(CharacterResponse(info = mockInfo, results = mockCharacters))
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        val result = repository.getAllCharacters(1).blockingGet()

        assertEquals(PaginatedResult(mockCharacters, hasNextPage = true), result)
    }

    @Test
    fun `getAllCharacters should return hasNextPage false when api indicates no next page`() {
        val mockInfo = createMockInfo(next = null)
        val mockCharacters = listOf(createMockCharacter(id = 1, name = "Rick"))
        val mockResponse = Response.success(CharacterResponse(info = mockInfo, results = mockCharacters))
        every { service.getCharacters(2) } returns Single.just(mockResponse)

        val result = repository.getAllCharacters(2).blockingGet()

        assertEquals(PaginatedResult(mockCharacters, hasNextPage = false), result)
    }

    @Test
    fun `getAllCharacters should throw RuntimeException for error responses`() {
        val errorBody = ResponseBody.create(null, """{ "error": "Some other error" }""")
        val mockResponse = Response.error<CharacterResponse>(500, errorBody)
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllCharacters(1).blockingGet()
        }
        assertEquals("Some other error", exception.message)
    }

    @Test
    fun `getAllCharacters should throw RuntimeException when response body is null`() {
        val mockResponse = Response.success<CharacterResponse>(null)
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllCharacters(1).blockingGet()
        }
        assertEquals("Response body is null", exception.message)
    }
}
