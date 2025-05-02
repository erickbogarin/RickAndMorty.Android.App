package com.example.rickandmorty.features.episodes.data.datasource

import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeRemoteDataSource {
    @GET("episode")
    fun getEpisodes(
        @Query("page") page: Int): Single<Response<EpisodesResponse>>
}