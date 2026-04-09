package com.example.rickandmorty.features.location.presentation

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.location.data.model.Location
import com.example.rickandmorty.features.location.domain.GetLocationsUseCase
import com.example.rickandmorty.utils.ViewModelTestExtension
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
class LocationViewModelTest {

    private val getLocationsUseCase: GetLocationsUseCase = mockk()
    private lateinit var viewModel: LocationViewModel

    @BeforeEach
    fun setup() {
        viewModel = LocationViewModel(getLocationsUseCase)
    }

    @Test
    fun `fetchLocations should request first page and append results`() {
        val location = createMockLocation(id = 1, name = "Earth")
        every { getLocationsUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(location), hasNextPage = true),
        )

        viewModel.fetchLocations()

        assertEquals(listOf(location), viewModel.locations.value)
        assertEquals(2, viewModel.paginationState.currentPage.value)
        assertFalse(viewModel.paginationState.isLoading.value ?: true)
    }

    @Test
    fun `fetchLocations should mark last page when result has no next page`() {
        every { getLocationsUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(createMockLocation(id = 1, name = "Earth")), hasNextPage = false),
        )

        viewModel.fetchLocations()

        assertTrue(viewModel.paginationState.isLastPage.value == true)
        assertTrue(viewModel.endOfList.value == true)
    }

    @Test
    fun `fetchLocations should not trigger request while loading`() {
        viewModel.paginationState.startLoading()

        viewModel.fetchLocations()

        verify(exactly = 0) { getLocationsUseCase.execute(any()) }
    }

    private fun createMockLocation(
        id: Int,
        name: String,
    ) = Location(
        id = id,
        name = name,
        type = "Planet",
        dimension = "Dimension C-137",
        residents = emptyList(),
        url = "https://example.com/location/$id",
        created = "2023-01-01T00:00:00Z",
    )
}
