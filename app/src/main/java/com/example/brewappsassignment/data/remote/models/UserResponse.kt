package com.example.brewappsassignment.data.remote.models

data class UserResponse(
    val id: String,
    val email: String,
    val user_metadata: UserMetadata?
)
