package com.example.rickandmorty.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.di.ViewModelFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ViewModelDelegate<T : BaseViewModel>(
    private val clazz: KClass<T>,
    private val activity: FragmentActivity,
    private val vmFactory: () -> ViewModelFactory
) : ReadWriteProperty<FragmentActivity, T> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        return ViewModelProvider(activity, vmFactory.invoke()).get(clazz.java).apply {
            thisRef.lifecycle.addObserver(this)
        }
    }

    override fun setValue(thisRef: FragmentActivity, property: KProperty<*>, value: T) {}
}
