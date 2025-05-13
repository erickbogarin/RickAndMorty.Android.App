package com.example.rickandmorty.features.episodes.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.commons.baseui.BaseViewModel
import com.example.rickandmorty.commons.exceptions.EndOfListException
import com.example.rickandmorty.commons.utils.pagination.PaginationCallback
import com.example.rickandmorty.commons.utils.pagination.PaginationState
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase,
) : BaseViewModel(), PaginationCallback {

    val episodes = MutableLiveData<List<EpisodeModel>>()
    val paginationState = PaginationState()
    val endOfList: LiveData<Boolean> get() = paginationState.endOfList

    @SuppressLint("CheckResult")
    override fun onLoadMore() {
        getEpisodes()
    }

    @SuppressLint("CheckResult")
    fun getEpisodes() {
        if (paginationState.isLastPage.value == true) return

        getEpisodesUseCase.execute(paginationState.currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newEpisodes ->
                    episodes.value = episodes.value.orEmpty() + newEpisodes
                },
                { error ->
                    if (error is EndOfListException) {
                        paginationState.markAsLastPage()
                    } else {
                        Log.e("Episodes", error.message.orEmpty())
                    }
                },
            )
    }

    fun refresh() {
        episodes.value = emptyList()
        paginationState.reset()
        getEpisodes()
    }
}
