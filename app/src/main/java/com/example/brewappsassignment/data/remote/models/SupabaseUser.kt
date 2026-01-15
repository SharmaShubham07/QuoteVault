package com.example.brewappsassignment.data.remote.models

data class SupabaseUser(
    val id: String?,
    val email: String?,
    val user_metadata: Map<String, Any>?
)