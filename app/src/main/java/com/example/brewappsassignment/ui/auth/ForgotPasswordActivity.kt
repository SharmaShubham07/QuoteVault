package com.example.brewappsassignment.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.brewappsassignment.databinding.ActivityForgotPasswordBinding
import com.google.android.material.snackbar.Snackbar

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClicks()
        observeState()
    }

    private fun setupClicks() {
        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            viewModel.resetPassword(email)
        }

        binding.ivBack.setOnClickListener { finish() }
    }

    private fun observeState() {
        viewModel.loading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) {
            if (it != null) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.success.observe(this) {
            if (it) {
                Snackbar.make(binding.root, "Password reset email sent!", Snackbar.LENGTH_LONG)
                    .show()
                finish()
            }
        }
    }
}
