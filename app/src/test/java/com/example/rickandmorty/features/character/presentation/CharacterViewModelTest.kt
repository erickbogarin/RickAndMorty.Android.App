package com.example.rickandmorty.features.character.presentation

import androidx.lifecycle.Observer
import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.ToggleFavoriteCharacterUseCase
import com.example.rickandmorty.features.character.domain.CheckFavoriteCharacterUseCase
import com.example.rickandmorty.utils.ViewModelTestExtension
import com.example.rickandmorty.utils.character.createMockCharacter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
            checkFavoriteCharacterUseCase,
        )
    }

    @Test
    fun `onLoadMore should request first page and append new characters to the list`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { checkFavoriteCharacterUseCase.execute(character) } returns false
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(character), hasNextPage = true),
        )

        viewModel.uiState.observeForever {}
        viewModel.onLoadMore()

        assertEquals(listOf(CharacterCardUiModel(character, false)), viewModel.uiState.value?.items)
        assertEquals(2, viewModel.paginationState.currentPage.value)
        assertFalse(viewModel.paginationState.isLoading.value ?: true)
    }

    @Test
    fun `onLoadMore should mark as last page when result has no next page`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { checkFavoriteCharacterUseCase.execute(character) } returns false
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(character), hasNextPage = false),
        )

        viewModel.onLoadMore()

        assertTrue(viewModel.paginationState.isLastPage.value == true)
    }

    @Test
    fun `onLoadMore should not trigger request when favorites filter is active`() {
        viewModel.setShowOnlyFavorites(true)

        viewModel.onLoadMore()

        verify(exactly = 0) { getCharactersUseCase.execute(any()) }
    }

    @Test
    fun `onLoadMore should not trigger a second request while loading`() {
        viewModel.paginationState.startLoading()

        viewModel.onLoadMore()

        verify(exactly = 0) { getCharactersUseCase.execute(any()) }
    }

    @Test
    fun `toggleFavorite should refresh uiState with updated favorite state`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(character), hasNextPage = true),
        )
        every { checkFavoriteCharacterUseCase.execute(character) } returnsMany listOf(false, true)
        every { toggleFavoriteCharacterUseCase.execute(character) } returns true

        val observer = mockk<Observer<CharacterUiState>>(relaxed = true)
        viewModel.uiState.observeForever(observer)
        viewModel.onLoadMore()

        viewModel.toggleFavorite(character)

        assertEquals(true, viewModel.uiState.value?.items?.single()?.isFavorite)
        verify(atLeast = 1) { observer.onChanged(any()) }
    }

    @Test
    fun `setShowOnlyFavorites should update uiState and filter only favorites`() {
        val favorite = createMockCharacter(id = 1, name = "Rick")
        val nonFavorite = createMockCharacter(id = 2, name = "Morty")
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(favorite, nonFavorite), hasNextPage = true),
        )
        every { checkFavoriteCharacterUseCase.execute(favorite) } returns true
        every { checkFavoriteCharacterUseCase.execute(nonFavorite) } returns false

        viewModel.onLoadMore()
        viewModel.setShowOnlyFavorites(true)

        assertTrue(viewModel.uiState.value?.showOnlyFavorites == true)
        assertEquals(listOf(CharacterCardUiModel(favorite, true)), viewModel.uiState.value?.items)
    }

    @Test
    fun `setShowOnlyFavorites should expose empty favorites message when needed`() {
        val character = createMockCharacter(id = 1, name = "Rick")
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(character), hasNextPage = true),
        )
        every { checkFavoriteCharacterUseCase.execute(character) } returns false

        viewModel.onLoadMore()
        viewModel.setShowOnlyFavorites(true)

        assertTrue(viewModel.uiState.value?.showEmptyFavoritesMessage == true)
    }
}
