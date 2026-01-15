package com.example.brewappsassignment.ui.auth

import androidx.lifecycle.*
import com.example.brewappsassignment.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val repo: AuthRepository) : ViewModel() {

    val loading = MutableLiveData(false)
    val signupSuccess = MutableLiveData(false)
    val error = MutableLiveData<String?>(null)

    fun signup(fullname: String, email: String, password: String) {

        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            error.value = "Please fill all fields"
            return
        }

        viewModelScope.launch {
            try {
                loading.value = true
                val ok = repo.signup(fullname, email, password)
                signupSuccess.value = ok
            } catch (e: Exception) {
                error.value = e.localizedMessage
            } finally {
                loading.value = false
            }
        }
    }

    class Factory(private val repo: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignupViewModel(repo) as T
        }
    }
}
