package com.example.rickandmorty.features.location.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.commons.baseui.BaseViewModel
import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.utils.pagination.PaginationCallback
import com.example.rickandmorty.commons.utils.pagination.PaginationState
import com.example.rickandmorty.features.location.data.model.Location
import com.example.rickandmorty.features.location.domain.GetLocationsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase
) : BaseViewModel(), PaginationCallback {

    val locations = MutableLiveData<List<Location>>()
    val paginationState = PaginationState()
    val isLastPage: LiveData<Boolean> get() = paginationState.isLastPage
    val endOfList: LiveData<Boolean> get() = paginationState.endOfList

    @SuppressLint("CheckResult")
    override fun onLoadMore() {
        fetchLocations()
    }

    @SuppressLint("CheckResult")
    fun fetchLocations() {
        if (paginationState.isLastPage.value == true) return

        getLocationsUseCase.execute(paginationState.currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newLocations ->
                    locations.value = locations.value.orEmpty() + newLocations
                },
                { error ->
                    if (error is EndOfListException) {
                        paginationState.markAsLastPage()
                    } else {
                        Log.e("LocationViewModel", error.message.orEmpty())
                    }
                }
            )
    }

    fun refresh() {
        locations.value = emptyList()
        paginationState.reset()
        fetchLocations()
    }
}