package com.example.rickandmorty.utils.episode

import com.example.rickandmorty.features.episodes.data.model.EpisodeModel

fun createMockEpisode(
    id: Int = 1,
    name: String = "Pilot",
    airDate: String = "December 2, 2013",
    episode: String = "S01E01",
    characters: List<String> = listOf("https://example.com/character/1"),
    url: String = "https://example.com/episode/1",
    created: String = "2023-01-01T00:00:00Z"
): EpisodeModel {
    return EpisodeModel(id, name, airDate, episode, characters, url, created)
}