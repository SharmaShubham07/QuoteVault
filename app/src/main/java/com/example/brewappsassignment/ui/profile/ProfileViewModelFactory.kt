package com.example.brewappsassignment.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.brewappsassignment.domain.repository.AuthRepository

class ProfileViewModelFactory(
    private val repo: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(repo) as T
    }
}
