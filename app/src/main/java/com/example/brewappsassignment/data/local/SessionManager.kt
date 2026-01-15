package com.example.brewappsassignment.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("auth_session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("access_token", null)

    fun saveRefreshToken(token: String) {
        prefs.edit().putString("refresh_token", token).apply()
    }

    fun saveTokens(access: String, refresh: String) {
        saveToken(access)
        saveRefreshToken(refresh)
    }

    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun saveUserId(id: String) {
        prefs.edit().putString("user_id", id).apply()
    }

    fun getUserId(): String? = prefs.getString("user_id", null)

    fun saveNotificationTime(hour: Int, minute: Int) {
        prefs.edit()
            .putInt("notif_hour", hour)
            .putInt("notif_min", minute)
            .apply()
    }

    fun getNotificationTime(): Pair<Int, Int> {
        val h = prefs.getInt("notif_hour", 9)
        val m = prefs.getInt("notif_min", 0)
        return h to m
    }

    fun saveTheme(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
    }

    fun getTheme(): String = prefs.getString("theme_mode", "light") ?: "light"

    fun saveAccent(accent: String) {
        prefs.edit().putString("accent", accent).apply()
    }

    fun getAccent(): String = prefs.getString("accent", "blue") ?: "blue"

    fun saveFontSize(size: Int) {
        prefs.edit().putInt("font_size", size).apply()
    }

    fun getFontSize(): Int = prefs.getInt("font_size", 16)


    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
