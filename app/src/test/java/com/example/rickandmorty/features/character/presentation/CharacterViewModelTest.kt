package com.example.rickandmorty.features.character.presentation

import androidx.lifecycle.Observer
import com.example.rickandmorty.commons.pagination.PaginatedResult
import com.example.rickandmorty.features.character.data.model.Character
import com.example.rickandmorty.features.character.domain.CheckFavoriteCharacterUseCase
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.ToggleFavoriteCharacterUseCase
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
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(character), hasNextPage = true),
        )

        viewModel.characters.observeForever {}
        viewModel.onLoadMore()

        assertEquals(listOf(character), viewModel.characters.value)
        assertEquals(2, viewModel.paginationState.currentPage.value)
        assertFalse(viewModel.paginationState.isLoading.value ?: true)
    }

    @Test
    fun `onLoadMore should mark as last page when result has no next page`() {
        every { getCharactersUseCase.execute(1) } returns Single.just(
            PaginatedResult(items = listOf(createMockCharacter(id = 1, name = "Rick")), hasNextPage = false),
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
