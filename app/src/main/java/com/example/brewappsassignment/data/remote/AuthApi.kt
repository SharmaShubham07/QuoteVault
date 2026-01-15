package com.example.brewappsassignment.data.remote

import com.example.brewappsassignment.data.remote.models.*
import retrofit2.http.*

interface AuthApi {

    @POST("auth/v1/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): SignupResponse

    @POST("auth/v1/token?grant_type=password")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/v1/recover")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    )

    @PATCH("auth/v1/user")
    suspend fun updateMetadata(
        @Header("Authorization") token: String,
        @Body body: Map<String, Any>
    )

    @POST("auth/v1/token?grant_type=refresh_token")
    suspend fun refreshToken(
        @Body body: Map<String, String>
    ): LoginResponse

    @POST("auth/v1/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    )

    /**
     * MUST pass the real user token (NEVER anon key)
     */
    @GET("auth/v1/user")
    suspend fun getUser(): UserResponse
}
