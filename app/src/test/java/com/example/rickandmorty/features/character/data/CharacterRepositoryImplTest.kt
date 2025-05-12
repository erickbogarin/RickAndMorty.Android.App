package com.example.rickandmorty.features.character.data

import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.exceptions.ErrorResponse
import com.example.rickandmorty.features.character.data.model.CharacterResponse
import com.example.rickandmorty.utils.character.createMockCharacter
import com.example.rickandmorty.utils.character.createMockInfo
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class CharacterRepositoryImplTest {

    private val service: CharacterApiService = mockk()
    private val repository = CharacterRepositoryImpl(service)

    @Test
    fun `getAllCharacters should return a list of characters when response is successful`() {
        // Arrange
        val mockInfo = createMockInfo()
        val mockCharacters = listOf(
            createMockCharacter(id = 1, name = "Rick"),
            createMockCharacter(id = 2, name = "Morty")
        )
        val mockResponse = Response.success(CharacterResponse(info = mockInfo, results = mockCharacters))
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        // Act
        val result = repository.getAllCharacters(1).blockingGet()

        // Assert
        assertEquals(mockCharacters, result)
    }

    @Test
    fun `getAllCharacters should throw EndOfListException when error response indicates end of list`() {
        // Arrange
        val errorResponse = ErrorResponse(error = "There is nothing here")
        val errorBody = ResponseBody.create(null, Gson().toJson(errorResponse))
        val mockResponse = Response.error<CharacterResponse>(404, errorBody)
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        // Act & Assert
        val exception = assertThrows(EndOfListException::class.java) {
            repository.getAllCharacters(1).blockingGet()
        }
        assertNotNull(exception)
    }

    @Test
    fun `getAllCharacters should throw RuntimeException for other error responses`() {
        // Arrange
        val errorJson = """{ "error": "Some other error" }""" // JSON válido
        val errorBody = ResponseBody.create(null, errorJson)
        val mockResponse = Response.error<CharacterResponse>(500, errorBody)
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        // Act & Assert
        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllCharacters(1).blockingGet()
        }
        assertEquals("Some other error", exception.message)
    }

    @Test
    fun `getAllCharacters should throw RuntimeException when response body is null`() {
        // Arrange
        val mockResponse = Response.success<CharacterResponse>(null)
        every { service.getCharacters(1) } returns Single.just(mockResponse)

        // Act & Assert
        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllCharacters(1).blockingGet()
        }
        assertEquals("Response body is null", exception.message)
    }
}