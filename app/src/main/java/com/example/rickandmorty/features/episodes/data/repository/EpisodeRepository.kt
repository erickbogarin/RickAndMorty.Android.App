package com.example.rickandmorty.features.episodes.data.repository

import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.commons.exceptions.ResourceException
import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

interface EpisodeRepository {
    fun getAllEpisodes(page: Int): Single<PaginatedResult<EpisodeModel>>
}

class EpisodeRepositoryImpl @Inject constructor(
    private val remoteDataSource: EpisodeRemoteDataSource,
) : EpisodeRepository {
    override fun getAllEpisodes(page: Int): Single<PaginatedResult<EpisodeModel>> {
        val response = remoteDataSource.getEpisodes(page)

        return response.map { response: Response<EpisodesResponse> ->
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
