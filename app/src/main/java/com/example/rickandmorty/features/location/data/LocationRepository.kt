package com.example.rickandmorty.features.location.data

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.commons.exceptions.ResourceException
import com.example.rickandmorty.features.location.data.model.Location
import com.example.rickandmorty.features.location.data.model.LocationResponse
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

interface LocationRepository {
    fun getAllLocations(page: Int): Single<PaginatedResult<Location>>
}

class LocationRepositoryImpl @Inject constructor(
    private val service: LocationApiService,
) : LocationRepository {
    override fun getAllLocations(page: Int): Single<PaginatedResult<Location>> {
        return service.getLocations(page).map { response: Response<LocationResponse> ->
            if (response.isSuccessful) {
                val body = response.body() ?: throw RuntimeException("Response body is null")
                PaginatedResult(
                    items = body.results,
                    hasNextPage = body.info.next != null,
                )
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResourceException::class.java)
                throw RuntimeException(errorResponse.error)
            }
        }
    }
}
