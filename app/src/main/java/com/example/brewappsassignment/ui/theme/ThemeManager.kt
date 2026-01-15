package com.example.brewappsassignment.ui.theme

import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    fun applyTheme(theme: String, accent: String) {

        // Apply light/dark mode
        when (theme) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Apply dynamic accent
        AccentManager.applyAccent(accent)
    }
}
