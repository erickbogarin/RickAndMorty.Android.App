package com.example.rickandmorty.features.character.di

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.config.ViewModelKey
import com.example.rickandmorty.features.character.data.CharacterApiService
import com.example.rickandmorty.features.character.data.CharacterRepository
import com.example.rickandmorty.features.character.data.CharacterRepositoryImpl
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.GetCharactersUseCaseImpl
import com.example.rickandmorty.features.character.presentation.CharacterFragment
import com.example.rickandmorty.features.character.presentation.CharacterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class CharacterModuleBuilder {

    @Singleton
    @ContributesAndroidInjector(
        modules = [
            CharacterModule::class
        ]
    )
    abstract fun provideCharacterFragment(): CharacterFragment
}

@Module
abstract class CharacterModule {
    @Binds
    @IntoMap
    @ViewModelKey(CharacterViewModel::class)
    abstract fun provideCharacterViewModel(viewModel: CharacterViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun provideGetCharactersUseCase(getCharactersUseCase: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    @Singleton
    abstract fun provideCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideCharacterApiService(retrofit: Retrofit): CharacterApiService =
            retrofit.create(CharacterApiService::class.java)
    }
}