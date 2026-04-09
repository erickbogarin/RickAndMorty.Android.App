package com.example.rickandmorty.features.episodes.domain

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.episodes.data.repository.EpisodeRepository
import com.example.rickandmorty.utils.episode.createMockEpisode
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetEpisodesUseCaseImplTest {

    private val repository: EpisodeRepository = mockk()
    private val useCase = GetEpisodesUseCaseImpl(repository)

    @Test
    fun `execute should return paginated episodes when repository call is successful`() {
        val mockEpisodes = listOf(
            createMockEpisode(id = 1, name = "Pilot"),
            createMockEpisode(id = 2, name = "Lawnmower Dog"),
        )
        val paginatedResult = PaginatedResult(mockEpisodes, hasNextPage = true)
        every { repository.getAllEpisodes(1) } returns Single.just(paginatedResult)

        val result = useCase.execute(1).blockingGet()

        assertEquals(paginatedResult, result)
    }

    @Test
    fun `execute should propagate error when repository call fails`() {
        val exception = RuntimeException("Repository error")
        every { repository.getAllEpisodes(1) } returns Single.error(exception)

        val thrownException = org.junit.jupiter.api.assertThrows<RuntimeException> {
            useCase.execute(1).blockingGet()
        }
        assertEquals("Repository error", thrownException.message)
    }
}
