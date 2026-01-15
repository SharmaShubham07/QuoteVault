package com.example.brewappsassignment.data.remote

import com.example.brewappsassignment.data.remote.models.FavoriteResponse
import com.example.brewappsassignment.data.remote.models.QuoteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuotesApi {

    @GET("rest/v1/quotes")
    suspend fun getQuotes(
        @Query("select") select: String = "*",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<QuoteDto>

    @GET("rest/v1/quotes")
    suspend fun searchQuotes(
        @Query("quote") quote: String? = null,
        @Query("author") author: String? = null,
        @Query("category") category: String? = null,
        @Query("select") select: String = "*"
    ): List<QuoteDto>
}
