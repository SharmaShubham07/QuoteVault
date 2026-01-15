package com.example.brewappsassignment.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.brewappsassignment.domain.repository.QuoteRepository

class FavoritesViewModelFactory(
    private val repo: QuoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repo) as T
    }
}
