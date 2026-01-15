package com.example.brewappsassignment.domain.repository

import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.AuthApi
import com.example.brewappsassignment.data.remote.models.*

class AuthRepository(
    private val api: AuthApi,
    private val session: SessionManager
) {

    // -------------------------------------------------------------
    // SIGNUP
    // -------------------------------------------------------------
    suspend fun signup(fullname: String, email: String, password: String): Boolean {

        val request = SignupRequest(
            email = email,
            password = password,
            data = UserMetadata(full_name = fullname)
        )

        val response = api.signup(request)

        // Supabase returns "id" when signup is successful
        if (response.id != null) {
            return true
        }

        throw Exception(response.error?.message ?: "Signup failed")
    }

    // -------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------
    suspend fun login(email: String, password: String): Boolean {

        val response = api.login(
            LoginRequest(email = email, password = password)
        )

        val accessToken = response.access_token
        val refreshToken = response.refresh_token
        val userId = response.user?.id

        if (accessToken != null && userId != null) {

            // Save auth data locally
            session.saveToken(accessToken)
            session.saveRefreshToken(refreshToken ?: "")
            session.saveUserId(userId)

            return true
        }

        throw Exception(response.error?.message ?: "Login failed")
    }

    // -------------------------------------------------------------
    // RESET PASSWORD
    // -------------------------------------------------------------
    suspend fun resetPassword(email: String) {
        api.resetPassword(
            ResetPasswordRequest(email = email)
        )
    }

    // -------------------------------------------------------------
    // GET CURRENT USER
    // -------------------------------------------------------------
    suspend fun getUserProfile(): UserResponse {

        val token = session.getToken()
            ?: throw Exception("Not logged in")

        return api.getUser()
    }

    suspend fun updateUserPreferences(theme: String, accent: String, fontSize: Int) {
        val token = session.getToken() ?: throw Exception("Not logged in")

        val updateBody = mapOf(
            "data" to mapOf(
                "theme" to theme,
                "accent" to accent,
                "font_size" to fontSize
            )
        )

        api.updateMetadata("Bearer $token", updateBody)
    }

    // -------------------------------------------------------------
    // LOGOUT
    // -------------------------------------------------------------
    suspend fun logout() {

        val token = session.getToken()

        if (token != null) {
            api.logout("Bearer $token")
        }

        // clear local session
        session.clearSession()
    }

    // -------------------------------------------------------------
    fun isLoggedIn(): Boolean = session.getToken() != null
}
