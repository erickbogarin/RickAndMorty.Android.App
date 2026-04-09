package com.example.rickandmorty.features.character.data

import android.annotation.SuppressLint
import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.commons.exceptions.ResourceException
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.data.model.CharacterResponse
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

interface CharacterRepository {
    fun getAllCharacters(page: Int): Single<PaginatedResult<Character>>
}

class CharacterRepositoryImpl @Inject constructor(
    private val service: CharacterApiService,
) : CharacterRepository {
    @SuppressLint("CheckResult")
    override fun getAllCharacters(page: Int): Single<PaginatedResult<Character>> {
        val response = service.getCharacters(page)

        return response.map { response: Response<CharacterResponse> ->
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
