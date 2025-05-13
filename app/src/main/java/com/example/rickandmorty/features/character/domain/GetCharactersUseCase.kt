package com.example.rickandmorty.features.character.domain

import com.example.rickandmorty.features.character.data.CharacterRepository
import com.example.rickandmorty.features.character.data.model.Character
import io.reactivex.Single
import javax.inject.Inject

interface GetCharactersUseCase {
    fun execute(page: Int): Single<List<Character>>
}

class GetCharactersUseCaseImpl @Inject constructor(
    private val repository: CharacterRepository,
) : GetCharactersUseCase {
    override fun execute(page: Int): Single<List<Character>> = repository.getAllCharacters(page)
}
