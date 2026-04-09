package com.example.rickandmorty.features.episodes.data

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import com.example.rickandmorty.features.episodes.data.repository.EpisodeRepositoryImpl
import com.example.rickandmorty.utils.episode.createMockEpisodesResponse
import com.example.rickandmorty.utils.episode.createMockInfo
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import retrofit2.Response

class EpisodeRepositoryImplTest {

    private val remoteDataSource: EpisodeRemoteDataSource = mockk()
    private val repository = EpisodeRepositoryImpl(remoteDataSource)

    @Test
    fun `getAllEpisodes should return paginated result when response is successful`() {
        val mockResponse = Response.success(
            createMockEpisodesResponse(
                info = createMockInfo(next = "https://rickandmortyapi.com/api/episode?page=2"),
            ),
        )
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        val result = repository.getAllEpisodes(1).blockingGet()

        assertEquals(PaginatedResult(mockResponse.body()!!.results, hasNextPage = true), result)
    }

    @Test
    fun `getAllEpisodes should return hasNextPage false when api indicates no next page`() {
        val mockResponse = Response.success(createMockEpisodesResponse())
        every { remoteDataSource.getEpisodes(2) } returns Single.just(mockResponse)

        val result = repository.getAllEpisodes(2).blockingGet()

        assertEquals(PaginatedResult(mockResponse.body()!!.results, hasNextPage = false), result)
    }

    @Test
    fun `getAllEpisodes should throw RuntimeException for error responses`() {
        val errorBody = ResponseBody.create(null, """{ "error": "Some other error" }""")
        val mockResponse = Response.error<EpisodesResponse>(500, errorBody)
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllEpisodes(1).blockingGet()
        }
        assertEquals("Some other error", exception.message)
    }

    @Test
    fun `getAllEpisodes should throw RuntimeException when response body is null`() {
        val mockResponse = Response.success<EpisodesResponse>(null)
        every { remoteDataSource.getEpisodes(1) } returns Single.just(mockResponse)

        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllEpisodes(1).blockingGet()
        }
        assertEquals("Response body is null", exception.message)
    }
}
