package com.example.brewappsassignment.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.AuthApi
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.data.remote.models.ResetPasswordRequest
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val api = SupabaseClient.instance.create(AuthApi::class.java)

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String?>(null)
    val success = MutableLiveData(false)

    fun resetPassword(email: String) {

        if (email.isEmpty()) {
            error.value = "Please enter your email"
            return
        }

        viewModelScope.launch {
            try {
                loading.value = true
                api.resetPassword(ResetPasswordRequest(email))
                success.value = true
            } catch (e: Exception) {
                error.value = e.message ?: "Something went wrong"
            } finally {
                loading.value = false
            }
        }
    }
}
