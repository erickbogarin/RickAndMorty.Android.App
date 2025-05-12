package com.example.rickandmorty.features.episodes.data.model

data class EpisodesResponse(
    val info: Info,
    val results: List<EpisodeModel>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class EpisodeModel(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)