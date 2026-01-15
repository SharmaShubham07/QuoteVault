package com.example.brewappsassignment.data.remote.models

data class SignupRequest(
    val email: String,
    val password: String,
    val data: UserMetadata
)