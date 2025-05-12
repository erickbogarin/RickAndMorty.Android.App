package com.example.rickandmorty.features.character.data

import android.annotation.SuppressLint
import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.exceptions.ErrorResponse
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.data.model.CharacterResponse
import com.google.gson.Gson
import javax.inject.Inject
import io.reactivex.Single
import retrofit2.Response

interface CharacterRepository {
    fun getAllCharacters(page: Int): Single<List<Character>>
}

class CharacterRepositoryImpl @Inject constructor(
    private val service: CharacterApiService
) : CharacterRepository {
    @SuppressLint("CheckResult")
    override fun getAllCharacters(page: Int): Single<List<Character>> {
        val response = service.getCharacters(page)

        return response.map { response: Response<CharacterResponse> ->
            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                if (errorResponse.error == "There is nothing here") {
                    throw EndOfListException()
                } else {
                    throw RuntimeException(errorResponse.error) // Extrai apenas a mensagem de erro
                }
            }
        }
    }
}