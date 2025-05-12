package com.example.rickandmorty.features.episodes.data

import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.exceptions.ErrorResponse
import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import com.example.rickandmorty.features.episodes.data.repository.EpisodeRepositoryImpl
import com.example.rickandmorty.utils.episode.createMockEpisodesResponse
import com.example.rickandmorty.utils.episode.createMockInfo
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class EpisodeRepositoryImplTest {

    private val remoteDataSource: EpisodeRemoteDataSource = mockk()
    private val repository = EpisodeRepositoryImpl(remoteDataSource)

    @Test
    fun `getAllEpisodes should return a list of episodes when response is successful`() {
        // Arrange
        val mockResponse = Response.success(createMockEpisodesResponse())
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        // Act
        val result = repository.getAllEpisodes(1).blockingGet()

        // Assert
        assertEquals(mockResponse.body()?.results, result)
    }

    @Test
    fun `getAllEpisodes should throw EndOfListException when error response indicates end of list`() {
        // Arrange
        val errorResponse = ErrorResponse(error = "There is nothing here")
        val errorBody = ResponseBody.create(null, Gson().toJson(errorResponse))
        val mockResponse = Response.error<EpisodesResponse>(404, errorBody)
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        // Act & Assert
        val exception = assertThrows(EndOfListException::class.java) {
            repository.getAllEpisodes(1).blockingGet()
        }
        assertNotNull(exception)
    }

    @Test
    fun `getAllEpisodes should throw RuntimeException for other error responses`() {
        // Arrange
        val errorJson = """{ "error": "Some other error" }"""
        val errorBody = ResponseBody.create(null, errorJson)
        val mockResponse = Response.error<EpisodesResponse>(500, errorBody)
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        // Act & Assert
        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllEpisodes(1).blockingGet()
        }
        assertEquals("Some other error", exception.message)
    }

    @Test
    fun `getAllEpisodes should throw RuntimeException when response body is null`() {
        // Arrange
        val mockResponse = Response.success<EpisodesResponse>(null)
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        // Act & Assert
        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllEpisodes(1).blockingGet()
        }
        assertEquals("Response body is null", exception.message)
    }
}