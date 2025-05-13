package com.example.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Corrigido aqui
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationManager(
    private val activity: AppCompatActivity,
    private val toolbar: Toolbar, // Corrigido aqui
    private val bottomNav: BottomNavigationView,
) {
    fun setup() {
        val navController = getNavController()
        setupAppBar(navController)
        setupBottomNavigation(navController)
    }

    private fun getNavController(): NavController {
        val navHostFragment = activity.supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }

    private fun setupAppBar(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mi_character, R.id.mi_episode, R.id.mi_location),
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = destination.label
        }
    }

    private fun setupBottomNavigation(navController: NavController) {
        bottomNav.setupWithNavController(navController)
    }
}
