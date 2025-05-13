package com.example.rickandmorty.commons.baseui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        // Para sobrescrever quando necessário
    }

    override fun onStart(owner: LifecycleOwner) {
        // Para sobrescrever quando necessário
    }

    override fun onResume(owner: LifecycleOwner) {
        // Para sobrescrever quando necessário
    }

    override fun onPause(owner: LifecycleOwner) {
        // Para sobrescrever quando necessário
    }

    override fun onStop(owner: LifecycleOwner) {
        // Para sobrescrever quando necessário
    }

    override fun onDestroy(owner: LifecycleOwner) {
        // Para sobrescrever quando necessário
    }

    protected val compositeDisposable = CompositeDisposable()

    operator fun CompositeDisposable.plus(d: Disposable) {
        this.add(d)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
