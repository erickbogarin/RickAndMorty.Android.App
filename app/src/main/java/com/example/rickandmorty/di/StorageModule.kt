package com.example.rickandmorty.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.rickandmorty.commons.serialization.GsonSerializer
import com.example.rickandmorty.commons.serialization.JsonSerializer
import com.example.rickandmorty.commons.storage.LocalStorage
import com.example.rickandmorty.commons.storage.SharedPreferencesStorage
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("rick_and_morty_prefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideLocalStorage(sharedPreferences: SharedPreferences): LocalStorage {
        return SharedPreferencesStorage(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideJsonSerializer(gson: Gson): JsonSerializer {
        return GsonSerializer(gson)
    }
}
