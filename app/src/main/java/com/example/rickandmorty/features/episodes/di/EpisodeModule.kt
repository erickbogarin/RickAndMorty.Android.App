package com.example.rickandmorty.features.episodes.di

import com.example.rickandmorty.features.character.data.CharacterRepository
import com.example.rickandmorty.features.character.data.CharacterRepositoryImpl
import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCaseImpl
import com.example.rickandmorty.features.episodes.presentation.EpisodeFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class EpisodeModuleBuilder {
    @ContributesAndroidInjector(
        modules = [
            EpisodeModule::class
        ]
    )
    abstract fun provideEpisodeFragment(): EpisodeFragment
}

@Module
abstract class EpisodeModule {
    @Binds
    @Singleton
    abstract fun provideCharacterRepository(repositoryImpl: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @Singleton
    abstract fun provideGetEpisodesUseCase(getEpisodesUseCase: GetEpisodesUseCaseImpl): GetEpisodesUseCase

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideEpisodeRemoteDataSource(retrofit: Retrofit): EpisodeRemoteDataSource =
            retrofit.create(EpisodeRemoteDataSource::class.java)
    }
}