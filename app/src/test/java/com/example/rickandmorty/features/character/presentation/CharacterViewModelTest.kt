package com.example.rickandmorty.features.character.presentation

import androidx.lifecycle.Observer
import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.domain.CheckFavoriteCharacterUseCase
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.ToggleFavoriteCharacterUseCase
import com.example.rickandmorty.utils.character.createMockCharacter
import com.example.rickandmorty.utils.ViewModelTestExtension
import io.mockk.*
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ViewModelTestExtension::class)
class CharacterViewModelTest {

    private val getCharactersUseCase: GetCharactersUseCase = mockk()
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase = mockk()
    private val checkFavoriteCharacterUseCase: CheckFavoriteCharacterUseCase = mockk()
    private lateinit var viewModel: CharacterViewModel

    @BeforeEach
    fun setup() {
        viewModel = CharacterViewModel(
            getCharactersUseCase,
            toggleFavoriteCharacterUseCase,
            checkFavoriteCharacterUseCase
        )
    }

    @Test
    fun `onLoadMore should append new characters to the list`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { getCharactersUseCase.execute(any()) } returns Single.just(listOf(character))

        viewModel.characters.observeForever {}

        viewModel.onLoadMore()

        assertEquals(listOf(character), viewModel.characters.value)
    }

    @Test
    fun `onLoadMore should mark as last page on EndOfListException`() {
        every { getCharactersUseCase.execute(any()) } returns Single.error(EndOfListException())

        viewModel.onLoadMore()

        assertTrue(viewModel.paginationState.isLastPage.value == true)
    }

    @Test
    fun `toggleFavorite should update favoriteStatus and refresh list if filtering favorites`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { toggleFavoriteCharacterUseCase.execute(character) } returns true
        viewModel.setShowOnlyFavorites(true)

        val observer = mockk<Observer<Pair<Character, Boolean>>>(relaxed = true)
        viewModel.favoriteStatus.observeForever(observer)

        viewModel.toggleFavorite(character)

        verify { observer.onChanged(character to true) }
    }

    @Test
    fun `isFavorite should delegate to use case`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { checkFavoriteCharacterUseCase.execute(character) } returns true

        assertTrue(viewModel.isFavorite(character))
    }

    @Test
    fun `setShowOnlyFavorites should update showOnlyFavorites LiveData`() {
        viewModel.setShowOnlyFavorites(true)
        assertTrue(viewModel.showOnlyFavorites.value == true)
        viewModel.setShowOnlyFavorites(false)
        assertTrue(viewModel.showOnlyFavorites.value == false)
    }
}