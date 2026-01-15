package com.example.brewappsassignment.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewappsassignment.data.remote.models.QuoteDto
import com.example.brewappsassignment.domain.repository.QuoteRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: QuoteRepository) : ViewModel() {

    val quotes = MutableLiveData<List<QuoteDto>>(emptyList())
    val qotd = MutableLiveData<QuoteDto>()
    val loading = MutableLiveData(false)
    val favorites = MutableLiveData<List<Int>>(emptyList())

    fun loadQOTD() {
        viewModelScope.launch {
            qotd.value = repo.getRandomQOTD()
        }
    }

    fun loadQuotes(refresh: Boolean = false) {
        viewModelScope.launch {
            loading.value = true
            val data = repo.getAllQuotes()
            quotes.value = data
            loading.value = false
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            loading.value = true
            quotes.value = repo.search(query)
            loading.value = false
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            loading.value = true
            quotes.value = repo.filterByCategory(category)
            loading.value = false
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val favQuotes = repo.getFavoriteQuotes()
            favorites.value = favQuotes.map { it.id }
        }
    }

    fun toggleFavorite(quote: QuoteDto) {
        viewModelScope.launch {
            val favList = favorites.value ?: emptyList()

            if (favList.contains(quote.id)) {
                repo.removeFavorite(quote.id)
            } else {
                repo.addFavorite(quote.id)
            }

            loadFavorites()
        }
    }

}
