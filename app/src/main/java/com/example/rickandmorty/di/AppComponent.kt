package com.example.rickandmorty.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.MyApplication
import com.example.rickandmorty.features.character.presentation.CharacterFragment
import com.example.rickandmorty.features.character.presentation.CharacterViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [AndroidInjectionModule::class, AppModule::class, ViewModelModule::class, NetworkModule::class])
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
