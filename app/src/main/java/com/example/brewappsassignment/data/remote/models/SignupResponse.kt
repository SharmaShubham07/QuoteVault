package com.example.brewappsassignment.data.remote.models

data class SignupResponse(
    val id: String?,
    val aud: String?,
    val role: String?,
    val email: String?,
    val confirmation_sent_at: String?,
    val user_metadata: UserMetadata?,
    val app_metadata: Map<String, Any>?,
    val created_at: String?,
    val updated_at: String?,
    val error: ErrorResponse? = null
)
