package com.example.rickandmorty.features.location.data

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.location.data.model.Info
import com.example.rickandmorty.features.location.data.model.Location
import com.example.rickandmorty.features.location.data.model.LocationResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import retrofit2.Response

class LocationRepositoryImplTest {

    private val service: LocationApiService = mockk()
    private val repository = LocationRepositoryImpl(service)

    @Test
    fun `getAllLocations should return paginated result when response is successful`() {
        val locations = listOf(createMockLocation(id = 1, name = "Earth"))
        val response = Response.success(
            LocationResponse(
                info = createMockInfo(next = "https://rickandmortyapi.com/api/location?page=2"),
                results = locations,
            ),
        )
        every { service.getLocations(1) } returns Single.just(response)

        val result = repository.getAllLocations(1).blockingGet()

        assertEquals(PaginatedResult(locations, hasNextPage = true), result)
    }

    @Test
    fun `getAllLocations should return hasNextPage false when next is null`() {
        val locations = listOf(createMockLocation(id = 1, name = "Earth"))
        val response = Response.success(
            LocationResponse(
                info = createMockInfo(next = null),
                results = locations,
            ),
        )
        every { service.getLocations(2) } returns Single.just(response)

        val result = repository.getAllLocations(2).blockingGet()

        assertEquals(PaginatedResult(locations, hasNextPage = false), result)
    }

    @Test
    fun `getAllLocations should throw runtime exception for error response`() {
        val errorBody = ResponseBody.create(null, """{ "error": "Some other error" }""")
        val response = Response.error<LocationResponse>(500, errorBody)
        every { service.getLocations(1) } returns Single.just(response)

        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllLocations(1).blockingGet()
        }

        assertEquals("Some other error", exception.message)
    }

    @Test
    fun `getAllLocations should throw runtime exception when body is null`() {
        val response = Response.success<LocationResponse>(null)
        every { service.getLocations(1) } returns Single.just(response)

        val exception = assertThrows(RuntimeException::class.java) {
            repository.getAllLocations(1).blockingGet()
        }

        assertEquals("Response body is null", exception.message)
    }

    private fun createMockInfo(
        count: Int = 1,
        pages: Int = 1,
        next: String? = null,
        prev: String? = null,
    ) = Info(count, pages, next, prev)

    private fun createMockLocation(
        id: Int,
        name: String,
        type: String = "Planet",
        dimension: String = "Dimension C-137",
        residents: List<String> = emptyList(),
        url: String = "https://example.com/location/$id",
        created: String = "2023-01-01T00:00:00Z",
    ) = Location(id, name, type, dimension, residents, url, created)
}
