package com.example.brewappsassignment.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.brewappsassignment.R
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.AuthApi
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.databinding.ActivityLoginBinding
import com.example.brewappsassignment.domain.repository.AuthRepository
import com.example.brewappsassignment.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupClicks()
        observeState()
        setupPasswordToggle()

        // Auto-login if session stored
        if (viewModel.isUserLoggedIn()) {
            goToHome()
        }
    }

    private fun setupViewModel() {
        val api = SupabaseClient.instance.create(AuthApi::class.java)
        val sessionManager = SessionManager(this)
        val repo = AuthRepository(api, sessionManager)

        viewModel = LoginViewModel(repo)
    }

    private fun setupClicks() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.login(email, password)
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.tvSignupLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun observeState() {

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { msg ->
            msg?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                goToHome()
            }
        }
    }

    private fun setupPasswordToggle() {
        binding.ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            binding.etPassword.inputType =
                if (isPasswordVisible)
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            binding.ivTogglePassword.setImageResource(
                if (isPasswordVisible) R.drawable.ic_eye
                else R.drawable.ic_eye_off
            )

            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}