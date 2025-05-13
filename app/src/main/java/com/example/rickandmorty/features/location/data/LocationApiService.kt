package com.example.rickandmorty.features.location.data

import com.example.rickandmorty.features.location.data.model.LocationResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {
    @GET("location")
    fun getLocations(
        @Query("page") page: Int,
    ): Single<Response<LocationResponse>>
}
