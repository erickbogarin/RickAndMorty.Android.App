package com.example.rickandmorty.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel(), LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        // To be overridden when necessary
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        // To be overridden when necessary
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        // To be overridden when necessary
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        // To be overridden when necessary
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        // To be overridden when necessary
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        // To be overridden when necessary
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
