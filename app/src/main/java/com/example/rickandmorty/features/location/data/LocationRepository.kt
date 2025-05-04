package com.example.rickandmorty.features.location.data

import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.exceptions.ErrorResponse
import com.example.rickandmorty.features.location.data.model.Location
import com.example.rickandmorty.features.location.data.model.LocationResponse
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

interface LocationRepository {
    fun getAllLocations(page: Int): Single<List<Location>>
}

class LocationRepositoryImpl @Inject constructor(
    private val service: LocationApiService
) : LocationRepository {
    override fun getAllLocations(page: Int): Single<List<Location>> {
        return service.getLocations(page).map { response: Response<LocationResponse> ->
            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                if (errorResponse.error == "There is nothing here") {
                    throw EndOfListException()
                } else {
                    throw RuntimeException(errorBody)
                }
            }
        }
    }
}