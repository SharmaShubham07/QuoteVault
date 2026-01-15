package com.example.brewappsassignment.ui.theme

import com.example.brewappsassignment.R

object AccentManager {

    var currentOverlay: Int = R.style.ThemeOverlay_Accent_Blue

    fun applyAccent(accent: String) {
        currentOverlay = when (accent) {
            "red" -> R.style.ThemeOverlay_Accent_Red
            "green" -> R.style.ThemeOverlay_Accent_Green
            else -> R.style.ThemeOverlay_Accent_Blue
        }
    }
}
