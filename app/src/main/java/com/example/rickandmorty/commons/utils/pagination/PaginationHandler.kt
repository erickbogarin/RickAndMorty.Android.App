package com.example.rickandmorty.commons.utils.pagination

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Interface for adapters that support pagination loading states
 */
interface PaginationAdapter {
    fun addLoadingView()
    fun removeLoadingView()
}

/**
 * Interface that the pagination component needs to communicate with its owner
 */
interface PaginationCallback {
    fun onLoadMore()
}

/**
 * Reusable pagination handler that manages the scroll listener and pagination logic
 *
 * @param recyclerView The RecyclerView that will use pagination
 * @param paginationState The state object that contains pagination information
 * @param adapter The Adapter that implements PaginationAdapter
 * @param callback Callback to notify when more items need to be loaded
 * @param debounceDelay Optional delay to debounce scroll events (default: 300ms)
 * @param tag Optional tag for logging (default: "PaginationHandler")
 */
class PaginationHandler(
    private val recyclerView: RecyclerView,
    private val paginationState: PaginationState,
    private val adapter: PaginationAdapter,
    private val callback: PaginationCallback,
    private val debounceDelay: Long = 300L,
    private val tag: String = "PaginationHandler"
) {
    private var debounceJob: Job? = null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            debounceJob?.cancel()
            debounceJob = CoroutineScope(Dispatchers.Main).launch {
                delay(debounceDelay)
                checkIfLoadMoreItems(recyclerView)
            }
        }
    }

    private fun checkIfLoadMoreItems(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (shouldLoadMoreItems(layoutManager)) {
            loadMoreItems()
        }
    }

    private fun shouldLoadMoreItems(layoutManager: LinearLayoutManager): Boolean {
        if (paginationState.isLastPage.value == true) return false

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val shouldLoad = visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= 0

        Log.d(tag, "shouldLoadMoreItems: $shouldLoad")
        return shouldLoad
    }

    private fun loadMoreItems() {
        adapter.addLoadingView()
        callback.onLoadMore()
        Log.d(tag, "Loading more items for page ${paginationState.currentPage.value}")
    }

    /**
     * Attach the scroll listener to the RecyclerView
     */
    fun attach() {
        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(scrollListener)
    }

    /**
     * Detach the scroll listener from the RecyclerView
     * Should be called in onDestroyView to prevent memory leaks
     */
    fun detach() {
        recyclerView.clearOnScrollListeners()
        debounceJob?.cancel()
    }
}