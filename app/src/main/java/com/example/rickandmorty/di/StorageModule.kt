package com.example.rickandmorty.di


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    fun provideGson(): Gson= Gson()

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("rick_and_morty_prefs", Context.MODE_PRIVATE)
    }
}