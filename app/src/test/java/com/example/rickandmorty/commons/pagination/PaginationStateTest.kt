package com.example.rickandmorty.commons.pagination

import com.example.rickandmorty.commons.utils.pagination.PaginationState
import com.example.rickandmorty.utils.ViewModelTestExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ViewModelTestExtension::class)
class PaginationStateTest {

    @Test
    fun `state should start on page one without loading or end flags`() {
        val state = PaginationState()

        assertEquals(1, state.currentPage.value)
        assertFalse(state.isLoading.value ?: true)
        assertFalse(state.isLastPage.value ?: true)
        assertFalse(state.endOfList.value ?: true)
    }

    @Test
    fun `markAsLastPage should update end flags`() {
        val state = PaginationState()

        state.markAsLastPage()

        assertTrue(state.isLastPage.value == true)
        assertTrue(state.endOfList.value == true)
    }

    @Test
    fun `reset should restore initial state`() {
        val state = PaginationState()
        state.startLoading()
        state.advancePage()
        state.markAsLastPage()

        state.reset()

        assertEquals(1, state.currentPage.value)
        assertFalse(state.isLoading.value ?: true)
        assertFalse(state.isLastPage.value ?: true)
        assertFalse(state.endOfList.value ?: true)
    }
}
