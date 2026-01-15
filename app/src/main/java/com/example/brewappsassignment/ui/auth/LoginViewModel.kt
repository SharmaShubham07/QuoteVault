package com.example.brewappsassignment.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewappsassignment.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String?>(null)
    val loginSuccess = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            error.value = "Please enter all fields"
            return
        }

        viewModelScope.launch {
            try {
                loading.value = true
                val ok = repo.login(email, password)
                loginSuccess.value = ok
            } catch (e: Exception) {
                error.value = e.message ?: "Something went wrong"
            } finally {
                loading.value = false
            }
        }
    }

    fun isUserLoggedIn(): Boolean = repo.isLoggedIn()
}