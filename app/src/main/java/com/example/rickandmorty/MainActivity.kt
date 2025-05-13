package com.example.rickandmorty

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.rickandmorty.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var themeManager: ThemeManager
    private lateinit var navigationManager: NavigationManager
    private var appAlreadyInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Recupera o estado salvo (se existir)
        if (savedInstanceState != null) {
            appAlreadyInitialized = savedInstanceState.getBoolean("appAlreadyInitialized", false)
        }

        var keepSplashOnScreen = !appAlreadyInitialized
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }

        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        injectDependencies()

        themeManager = ThemeManager(this)
        navigationManager = NavigationManager(this, viewBinding.toolbar, viewBinding.appNavigation)

        setSupportActionBar(viewBinding.toolbar)
        navigationManager.setup()
        themeManager.setupThemeSwitch(viewBinding.themeSwitch)

        if (!appAlreadyInitialized) {
            lifecycleScope.launch {
                initializeApp()
                appAlreadyInitialized = true
                keepSplashOnScreen = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("appAlreadyInitialized", appAlreadyInitialized)
    }

    private suspend fun initializeApp() {
        delay(1200)
    }

    private fun injectDependencies() {
        AndroidInjection.inject(this)
    }
}
