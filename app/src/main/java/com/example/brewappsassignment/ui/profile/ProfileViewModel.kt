package com.example.brewappsassignment.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewappsassignment.data.remote.models.UserResponse
import com.example.brewappsassignment.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: AuthRepository) : ViewModel() {

    val user = MutableLiveData<UserResponse?>()
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String?>(null)

    fun loadProfile() {
        viewModelScope.launch {
            loading.postValue(true)
            error.postValue(null)

            try {
                val response = repo.getUserProfile()
                user.postValue(response)
            } catch (e: Exception) {
                error.postValue(e.message ?: "Failed to load profile")
            } finally {
                loading.postValue(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repo.logout()
            } catch (_: Exception) {
                // Even if logout API fails, clear local session anyway.
            }
        }
    }
}
