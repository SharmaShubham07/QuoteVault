package com.example.brewappsassignment.data.remote.models
data class LoginResponse(
    val access_token: String?,
    val token_type: String?,
    val expires_in: Int?,
    val refresh_token: String?,
    val user: SupabaseUser?,
    val error: SupabaseError?
)
