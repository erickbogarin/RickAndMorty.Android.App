package com.example.rickandmorty.features.episodes.data.repository

import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

interface EpisodeRepository {
    fun getAllEpisodes(page: Int): Single<List<EpisodeModel>>
}

class EpisodeRepositoryImpl @Inject constructor(
    private val remoteDataSource: EpisodeRemoteDataSource
) : EpisodeRepository {
    override fun getAllEpisodes(page: Int): Single<List<EpisodeModel>> {
        val response = remoteDataSource.getEpisodes(page)

        return response.map { response: Response<EpisodesResponse> ->
            if (response.isSuccessful) {
                return@map response.body()?.results ?: throw RuntimeException("Response body is null")
            }

            throw RuntimeException(response.errorBody()?.string())
        }
    }
}
