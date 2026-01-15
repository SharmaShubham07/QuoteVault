package com.example.brewappsassignment.data.remote

import com.example.brewappsassignment.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    private lateinit var sessionManager: SessionManager

    fun init(session: SessionManager) {
        sessionManager = session
    }

    /**
     * Common headers for ALL Supabase REST requests
     */
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newRequest = original.newBuilder()

        val anonKey = SupabaseConstants.SUPABASE_ANON_KEY
        val accessToken = sessionManager.getToken()

        newRequest.addHeader("apikey", anonKey)
        newRequest.addHeader(
            "Authorization",
            if (accessToken.isNullOrEmpty()) "Bearer $anonKey" else "Bearer $accessToken"
        )
        newRequest.addHeader("Accept", "application/json")
        newRequest.addHeader("Content-Type", "application/json")

        chain.proceed(newRequest.build())
    }

    /**
     * Refresh token logic – ONLY for PostgREST endpoints (rest/v1)
     */
    private val refreshInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        // Only refresh if 401 and NOT auth endpoints
        val isAuthUrl = request.url.toString().contains("/auth/v1")

        if (response.code == 401 && !isAuthUrl) {

            val refreshToken = sessionManager.getRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {

                val tokenResponse = runBlocking {
                    try {
                        authApi.refreshToken(
                            mapOf("refresh_token" to refreshToken)
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                if (tokenResponse != null && !tokenResponse.access_token.isNullOrEmpty()) {

                    // Save new tokens
                    sessionManager.saveTokens(
                        tokenResponse.access_token!!,
                        tokenResponse.refresh_token ?: refreshToken
                    )

                    // Retry original request with new token
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer ${tokenResponse.access_token}")
                        .build()

                    return@Interceptor chain.proceed(newRequest)
                }
            }
        }

        return@Interceptor response
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(refreshInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("${SupabaseConstants.SUPABASE_URL}/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { instance.create(AuthApi::class.java) }
}
