package com.example.rickandmorty.features.character.di

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.commons.di.FragmentScope
import com.example.rickandmorty.config.ViewModelKey
import com.example.rickandmorty.features.character.data.CharacterApiService
import com.example.rickandmorty.features.character.data.CharacterRepository
import com.example.rickandmorty.features.character.data.CharacterRepositoryImpl
import com.example.rickandmorty.features.character.data.FavoriteCharacterRepository
import com.example.rickandmorty.features.character.data.FavoriteCharacterRepositoryImpl
import com.example.rickandmorty.features.character.domain.GetCharactersUseCase
import com.example.rickandmorty.features.character.domain.GetCharactersUseCaseImpl
import com.example.rickandmorty.features.character.domain.GetFavoritesUseCase
import com.example.rickandmorty.features.character.domain.GetFavoritesUseCaseImpl
import com.example.rickandmorty.features.character.presentation.CharacterFragment
import com.example.rickandmorty.features.character.presentation.CharacterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class CharacterModuleBuilder {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            CharacterModule::class,
        ],
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
    @FragmentScope
    abstract fun provideGetCharactersUseCase(getCharactersUseCase: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    @FragmentScope
    abstract fun provideCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @FragmentScope
    abstract fun provideGetFavoritesUseCase(getFavoritesUseCase: GetFavoritesUseCaseImpl): GetFavoritesUseCase

    @Binds
    @FragmentScope
    abstract fun provideFavoriteCharacterRepository(
        repository: FavoriteCharacterRepositoryImpl,
    ): FavoriteCharacterRepository

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideCharacterApiService(retrofit: Retrofit): CharacterApiService =
            retrofit.create(CharacterApiService::class.java)
    }
}
