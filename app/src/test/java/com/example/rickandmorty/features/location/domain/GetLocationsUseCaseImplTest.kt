package com.example.rickandmorty.features.location.domain

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.location.data.LocationRepository
import com.example.rickandmorty.features.location.data.model.Location
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetLocationsUseCaseImplTest {

    private val repository: LocationRepository = mockk()
    private val useCase = GetLocationsUseCaseImpl(repository)

    @Test
    fun `execute should return paginated locations when repository succeeds`() {
        val locations = listOf(
            Location(
                id = 1,
                name = "Earth",
                type = "Planet",
                dimension = "Dimension C-137",
                residents = emptyList(),
                url = "https://example.com/location/1",
                created = "2023-01-01T00:00:00Z",
            ),
        )
        val paginatedResult = PaginatedResult(locations, hasNextPage = true)
        every { repository.getAllLocations(1) } returns Single.just(paginatedResult)

        val result = useCase.execute(1).blockingGet()

        assertEquals(paginatedResult, result)
    }
}
