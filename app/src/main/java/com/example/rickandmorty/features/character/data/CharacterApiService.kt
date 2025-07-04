package com.example.rickandmorty.features.character.data

import com.example.rickandmorty.features.character.data.model.CharacterResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApiService {
    @GET("character")
    fun getCharacters(
        @Query("page") page: Int,
    ): Single<Response<CharacterResponse>>
}
