package com.example.brewappsassignment.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewappsassignment.data.remote.models.QuoteDto
import com.example.brewappsassignment.domain.repository.QuoteRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repo: QuoteRepository) : ViewModel() {

    val favorites = MutableLiveData<List<QuoteDto>>(emptyList())
    val loading = MutableLiveData(false)

    fun loadFavorites() {
        viewModelScope.launch {
            loading.value = true
            favorites.value = repo.getFavoriteQuotes()
            loading.value = false
        }
    }
}
