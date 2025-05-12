package com.example.rickandmorty.features.episodes.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmorty.base.BaseViewModel
import com.example.rickandmorty.features.episodes.data.model.EpisodeModel
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase
) : BaseViewModel() {

    val episodes = MutableLiveData<List<EpisodeModel>>()
    private val _currentPage = MutableLiveData(0)
    private val _pageSize = MutableLiveData(10)

    val currentPage: LiveData<Int> get() = _currentPage

    fun incrementCurrentPage() {
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }

    @SuppressLint("CheckResult")
    fun getEpisodes() {
        getEpisodesUseCase.execute(_currentPage.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newEpisodes ->
                    episodes.value = episodes.value.orEmpty() + newEpisodes
                },
                { error ->
                    Log.e("Episodes", error.message.orEmpty())
                }
            )
    }
}