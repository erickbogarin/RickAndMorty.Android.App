package com.example.rickandmorty.commons.utils.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * A composable component that maintains pagination state
 * Can be included in any ViewModel without changing inheritance
 */
class PaginationState {
    private val _currentPage = MutableLiveData(1)
    private val _isLoading = MutableLiveData(false)
    private val _isLastPage = MutableLiveData(false)
    private val _endOfList = MutableLiveData(false)

    val currentPage: LiveData<Int> get() = _currentPage
    val isLoading: LiveData<Boolean> get() = _isLoading
    val isLastPage: LiveData<Boolean> get() = _isLastPage
    val endOfList: LiveData<Boolean> get() = _endOfList

    fun startLoading() {
        _isLoading.value = true
    }

    fun finishLoading() {
        _isLoading.value = false
    }

    fun advancePage() {
        _currentPage.value = (_currentPage.value ?: 1) + 1
    }

    /**
     * Mark the pagination as complete (no more pages available)
     */
    fun markAsLastPage() {
        _isLastPage.value = true
        _endOfList.value = true
    }

    fun reset() {
        _currentPage.value = 1
        _isLoading.value = false
        _isLastPage.value = false
        _endOfList.value = false
    }
}
