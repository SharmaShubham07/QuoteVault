package com.example.brewappsassignment.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.brewappsassignment.R
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.AuthApi
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.databinding.ActivitySignupBinding
import com.example.brewappsassignment.domain.repository.AuthRepository
import com.google.android.material.snackbar.Snackbar

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var isPasswordVisible = false

    private val viewModel: SignupViewModel by viewModels {
        SignupViewModel.Factory(
            AuthRepository(
                SupabaseClient.instance.create(AuthApi::class.java),
                SessionManager(this)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClicks()
        observeState()
    }

    private fun setupClicks() {

        binding.ivBack.setOnClickListener { finish() }

        binding.btnSignup.setOnClickListener {
            val fullname = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.signup(fullname, email, password)
        }

        binding.tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.ivTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        if (isPasswordVisible) {
            binding.etPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.ivTogglePassword.setImageResource(R.drawable.ic_eye)
        } else {
            binding.etPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.ivTogglePassword.setImageResource(R.drawable.ic_eye_off)
        }

        binding.etPassword.setSelection(binding.etPassword.text.length)
    }

    private fun observeState() {

        viewModel.loading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { msg ->
            msg?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.signupSuccess.observe(this) {
            if (it) {
                Snackbar.make(binding.root, "Account created successfully!", Snackbar.LENGTH_LONG).show()

                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
