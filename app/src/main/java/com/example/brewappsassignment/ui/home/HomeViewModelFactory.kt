package com.example.brewappsassignment.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.brewappsassignment.domain.repository.QuoteRepository

class HomeViewModelFactory(
    private val repo: QuoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}
