package com.example.rickandmorty.features.episodes.domain

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
    fun `execute should return a list of episodes when repository call is successful`() {
        // Arrange
        val mockEpisodes = listOf(
            createMockEpisode(id = 1, name = "Pilot"),
            createMockEpisode(id = 2, name = "Lawnmower Dog"),
        )
        every { repository.getAllEpisodes(1) } returns Single.just(mockEpisodes)

        // Act
        val result = useCase.execute(1).blockingGet()

        // Assert
        assertEquals(mockEpisodes, result)
    }

    @Test
    fun `execute should propagate error when repository call fails`() {
        // Arrange
        val exception = RuntimeException("Repository error")
        every { repository.getAllEpisodes(1) } returns Single.error(exception)

        // Act & Assert
        val thrownException = org.junit.jupiter.api.assertThrows<RuntimeException> {
            useCase.execute(1).blockingGet()
        }
        assertEquals("Repository error", thrownException.message)
    }
}
