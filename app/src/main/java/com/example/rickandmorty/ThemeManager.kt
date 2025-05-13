package com.example.rickandmorty

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class ThemeManager(private val activity: AppCompatActivity) {
    fun setupThemeSwitch(themeSwitch: SwitchCompat) {
        // Configure o estado inicial
        themeSwitch.isChecked = isDarkThemeActive()

        // Adiciona listener
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            applyTheme(isChecked)
        }
    }

    private fun isDarkThemeActive(): Boolean {
        return when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.delegate.localNightMode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                },
            )
        }
    }
}
