package com.example.rickandmorty.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.MyApplication
import com.example.rickandmorty.features.character.di.CharacterModuleBuilder
import com.example.rickandmorty.features.episodes.di.EpisodeModuleBuilder
import com.example.rickandmorty.features.location.di.LocationModuleBuilder
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Provider
import javax.inject.Singleton

@Module
abstract class AppModule {
    @ContributesAndroidInjector(
        modules = [CharacterModuleBuilder::class, EpisodeModuleBuilder::class, LocationModuleBuilder::class]
    )
    abstract fun provideMainActivity(): MainActivity

    @Module
    companion object {
        @Provides
        @Singleton
        fun provideViewModelFactoryProviders(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = ViewModelFactory(providers)
    }
}
