package com.example.rickandmorty.features.episodes.presentation

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import com.example.rickandmorty.utils.ViewModelTestExtension
import com.example.rickandmorty.utils.episode.createMockEpisode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ViewModelTestExtension::class)
class EpisodeViewModelTest {

    private val getEpisodesUseCase: GetEpisodesUseCase = mockk()
    private lateinit var viewModel: EpisodeViewModel

    @BeforeEach
    fun setup() {
        viewModel = EpisodeViewModel(getEpisodesUseCase)
    }

    @Test
    fun `getEpisodes should request first page and append results`() {
        val episode = createMockEpisode(id = 1, name = "Pilot")
        every { getEpisodesUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(episode), hasNextPage = true),
        )

        viewModel.getEpisodes()

        assertEquals(listOf(episode), viewModel.episodes.value)
        assertEquals(2, viewModel.paginationState.currentPage.value)
        assertFalse(viewModel.paginationState.isLoading.value ?: true)
    }

    @Test
    fun `getEpisodes should mark last page when result has no next page`() {
        every { getEpisodesUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(createMockEpisode()), hasNextPage = false),
        )

        viewModel.getEpisodes()

        assertTrue(viewModel.paginationState.isLastPage.value == true)
        assertTrue(viewModel.endOfList.value == true)
    }

    @Test
    fun `getEpisodes should not trigger a second request while loading`() {
        viewModel.paginationState.startLoading()

        viewModel.getEpisodes()

        verify(exactly = 0) { getEpisodesUseCase.execute(any()) }
    }
}
