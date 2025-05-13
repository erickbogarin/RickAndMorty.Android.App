package com.example.rickandmorty.utils.episode

import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.data.model.EpisodesResponse
import com.example.rickandmorty.features.episodes.data.model.Info

fun createMockInfo(
    count: Int = 2,
    pages: Int = 1,
    next: String? = null,
    prev: String? = null,
): Info {
    return Info(count, pages, next, prev)
}

fun createMockEpisode(
    id: Int = 1,
    name: String = "Pilot",
    airDate: String = "December 2, 2013",
    episode: String = "S01E01",
    characters: List<String> = listOf("https://example.com/character/1"),
    url: String = "https://example.com/episode/1",
    created: String = "2023-01-01T00:00:00Z",
): EpisodeModel {
    return EpisodeModel(id, name, airDate, episode, characters, url, created)
}

fun createMockEpisodesResponse(
    info: Info = createMockInfo(),
    episodes: List<EpisodeModel> = listOf(
        createMockEpisode(id = 1, name = "Pilot"),
        createMockEpisode(id = 2, name = "Lawnmower Dog"),
    ),
): EpisodesResponse {
    return EpisodesResponse(info, episodes)
}
