package com.example.rickandmorty.features.episodes.data.repository

import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

interface EpisodeRepository {
    fun getAllEpisodes(page: Int): Single<List<EpisodeModel>>
}

class EndOfListException : RuntimeException()

data class ErrorResponse(val error: String)

class EpisodeRepositoryImpl @Inject constructor(
    private val remoteDataSource: EpisodeRemoteDataSource
) : EpisodeRepository {
    override fun getAllEpisodes(page: Int): Single<List<EpisodeModel>> {
        val response = remoteDataSource.getEpisodes(page)

        return response.map { response: Response<EpisodesResponse> ->
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
