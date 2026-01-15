package com.example.brewappsassignment.data.remote

import com.example.brewappsassignment.data.remote.models.FavoriteRequest
import com.example.brewappsassignment.data.remote.models.FavoriteResponse
import retrofit2.http.*

interface FavoritesApi {

    @Headers("Content-Type: application/json")
    @POST("rest/v1/user_favorites")
    suspend fun addFavorite(
        @Body request: FavoriteRequest
    )

    @DELETE("rest/v1/user_favorites")
    suspend fun removeFavorite(
        @Query("quote_id") quoteId: String,
        @Query("user_id") userId: String
    ): retrofit2.Response<Unit>

    @GET("rest/v1/user_favorites")
    suspend fun getFavorites(
        @Query("user_id") userId: String,
        @Query("select") select: String = "quotes(*)"
    ): List<FavoriteResponse>
}