package com.example.rickandmorty.base

import androidx.lifecycle.Observer

class SafeObserver<T>(private val observer: Observer<T>) : Observer<T> {
    override fun onChanged(data: T) {
        observer.onChanged(data)
    }
}