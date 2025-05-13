package com.example.rickandmorty.features.episodes.domain

import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.data.repository.EpisodeRepository
import io.reactivex.Single
import javax.inject.Inject

interface GetEpisodesUseCase {
    fun execute(page: Int): Single<List<EpisodeModel>>
}

class GetEpisodesUseCaseImpl @Inject constructor(
    private val repository: EpisodeRepository,
) : GetEpisodesUseCase {
    override fun execute(page: Int): Single<List<EpisodeModel>> = repository.getAllEpisodes(page)
}
