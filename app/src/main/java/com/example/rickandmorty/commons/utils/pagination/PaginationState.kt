package com.example.rickandmorty.commons.utils.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * A composable component that maintains pagination state
 * Can be included in any ViewModel without changing inheritance
 */
class PaginationState {
    private val _currentPage = MutableLiveData(0)
    private val _isLastPage = MutableLiveData(false)
    private val _endOfList = MutableLiveData(false)

    val currentPage: LiveData<Int> get() = _currentPage
    val isLastPage: LiveData<Boolean> get() = _isLastPage
    val endOfList: LiveData<Boolean> get() = _endOfList

    /**
     * Increment the current page counter
     */
    fun incrementCurrentPage() {
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }

    /**
     * Mark the pagination as complete (no more pages available)
     */
    fun markAsLastPage() {
        _isLastPage.value = true
        _endOfList.value = true
    }

    /**
     * Reset pagination state
     * Useful when implementing pull-to-refresh or changing filters
     */
    fun reset() {
        _currentPage.value = 0
        _isLastPage.value = false
        _endOfList.value = false
    }
}
