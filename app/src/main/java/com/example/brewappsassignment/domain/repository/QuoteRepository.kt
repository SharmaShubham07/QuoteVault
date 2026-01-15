package com.example.brewappsassignment.domain.repository

import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.FavoritesApi
import com.example.brewappsassignment.data.remote.QuotesApi
import com.example.brewappsassignment.data.remote.models.FavoriteRequest
import com.example.brewappsassignment.data.remote.models.QuoteDto

class QuoteRepository(
    private val quotesApi: QuotesApi,
    private val favoritesApi: FavoritesApi,
    private val session: SessionManager
) {

    suspend fun getAllQuotes(): List<QuoteDto> {
        return quotesApi.getQuotes(limit = 500, offset = 0)
    }

    suspend fun getRandomQOTD(): QuoteDto {
        return quotesApi.getQuotes(limit = 500, offset = 0).random()
    }

    suspend fun search(query: String): List<QuoteDto> {
        return quotesApi.searchQuotes(quote = "ilike.%$query%")
    }

    suspend fun filterByCategory(category: String): List<QuoteDto> {
        return quotesApi.searchQuotes(category = "eq.$category")
    }

    // ------------------------------------------------------
    // FAVORITES
    // ------------------------------------------------------

    suspend fun addFavorite(quoteId: Int) {
        val userId = session.getUserId() ?: return

        try {
            favoritesApi.addFavorite(
                FavoriteRequest(
                    user_id = userId,
                    quote_id = quoteId
                )
            )
        } catch (e: Exception) {
            if (e is retrofit2.HttpException && e.code() == 409) {
                return
            } else {
                throw e
            }
        }
    }

    suspend fun removeFavorite(quoteId: Int) {
        val userId = session.getUserId() ?: return

        favoritesApi.removeFavorite(
            quoteId = "eq.$quoteId",
            userId = "eq.$userId"
        )
    }

    suspend fun getFavoriteQuotes(): List<QuoteDto> {
        val userId = session.getUserId() ?: return emptyList()

        return favoritesApi.getFavorites(
            userId = "eq.$userId"
        ).map { it.quotes }
    }
}
