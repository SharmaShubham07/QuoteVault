package com.example.brewappsassignment.data.remote.models

data class ErrorResponse(
    val message: String? = null,
    val error: String? = null,
    val error_description: String? = null
)
