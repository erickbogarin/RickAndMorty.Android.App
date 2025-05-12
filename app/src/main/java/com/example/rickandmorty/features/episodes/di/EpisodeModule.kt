package com.example.rickandmorty.features.episodes.di

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.config.ViewModelKey
import com.example.rickandmorty.features.character.data.CharacterRepository
import com.example.rickandmorty.features.character.data.CharacterRepositoryImpl
import com.example.rickandmorty.features.episodes.data.datasource.EpisodeRemoteDataSource
import com.example.rickandmorty.features.episodes.data.repository.EpisodeRepository
import com.example.rickandmorty.features.episodes.data.repository.EpisodeRepositoryImpl
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import com.example.rickandmorty.features.episodes.domain.GetEpisodesUseCaseImpl
import com.example.rickandmorty.features.episodes.presentation.EpisodeFragment
import com.example.rickandmorty.features.episodes.presentation.EpisodeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class EpisodeModuleBuilder {
    @Singleton
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
    @IntoMap
    @ViewModelKey(EpisodeViewModel::class)
    abstract fun provideEpisodeViewModel(viewModel: EpisodeViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun provideGetEpisodesUseCase(getEpisodesUseCase: GetEpisodesUseCaseImpl): GetEpisodesUseCase

    @Binds
    @Singleton
    abstract fun provideEpisodeRepository(repositoryImpl: EpisodeRepositoryImpl): EpisodeRepository

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideEpisodeRemoteDataSource(retrofit: Retrofit): EpisodeRemoteDataSource =
            retrofit.create(EpisodeRemoteDataSource::class.java)
    }
}