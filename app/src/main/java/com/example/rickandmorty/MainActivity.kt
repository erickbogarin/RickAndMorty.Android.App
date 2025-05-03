package com.example.rickandmorty

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashOnScreen = true

        // Instale a splash e defina a condição para mantê-la
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }

        super.onCreate(savedInstanceState)
        // Inicializa o binding
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        lifecycleScope.launch {
            // Faça suas inicializações aqui
            initializeApp()

            // Quando terminar, permita que a splash screen seja removida
            keepSplashOnScreen = false
        }

        // Injeta dependências
        injectDependencies()

        // Configura a Toolbar e a navegação
        setupToolbar()
        setupNavigation()
    }

    private suspend fun initializeApp() {
        // Código de inicialização do app
        delay(1500) // Exemplo de inicialização que leva algum tempo
    }

    private fun injectDependencies() {
        AndroidInjection.inject(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(viewBinding.toolbar)
    }

    private fun setupNavigation() {
        val navController = getNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mi_character,
                R.id.mi_episode,
                R.id.mi_location
            )
        )

        viewBinding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Atualiza o título com o título do fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewBinding.toolbar.title = destination.label
        }

        // Configura o BottomNavigation
        viewBinding.appNavigation.setupWithNavController(navController)

        // Define o título inicial
        viewBinding.toolbar.title = navController.currentDestination?.label
    }

    private fun getNavController() : NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }
}