package com.example.brewappsassignment.data.remote.models

data class QuoteDto(
    val id: Int,
    val text: String,
    val author: String,
    val category: String,
    val created_at: String,
    var isFavorite: Boolean = false
)
