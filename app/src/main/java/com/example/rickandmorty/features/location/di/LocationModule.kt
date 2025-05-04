package com.example.rickandmorty.features.location.di

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.config.ViewModelKey
import com.example.rickandmorty.features.location.data.LocationApiService
import com.example.rickandmorty.features.location.data.LocationRepository
import com.example.rickandmorty.features.location.data.LocationRepositoryImpl
import com.example.rickandmorty.features.location.domain.GetLocationsUseCase
import com.example.rickandmorty.features.location.domain.GetLocationsUseCaseImpl
import com.example.rickandmorty.features.location.presentation.LocationFragment
import com.example.rickandmorty.features.location.presentation.LocationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class LocationModuleBuilder {
    @Singleton
    @ContributesAndroidInjector(
        modules = [LocationModule::class]
    )
    abstract fun provideLocationFragment(): LocationFragment
}

@Module
abstract class LocationModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    abstract fun provideLocationViewModel(viewModel: LocationViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun provideGetLocationsUseCase(getLocationsUseCase: GetLocationsUseCaseImpl): GetLocationsUseCase

    @Binds
    @Singleton
    abstract fun provideLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideLocationApiService(retrofit: Retrofit): LocationApiService =
            retrofit.create(LocationApiService::class.java)
    }
}