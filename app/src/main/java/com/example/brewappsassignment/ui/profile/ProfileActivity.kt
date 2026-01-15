package com.example.brewappsassignment.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.brewappsassignment.R
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.databinding.ActivityProfileBinding
import com.example.brewappsassignment.domain.repository.AuthRepository
import com.example.brewappsassignment.ui.auth.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var session: SessionManager

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            AuthRepository(
                SupabaseClient.authApi,
                SessionManager(this)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        setupClicks()
        observeData()

        viewModel.loadProfile()
    }

    private fun setupClicks() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            session.clearSession()

            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    private fun observeData() {

        viewModel.user.observe(this) { user ->
            if (user != null) {

                // Name
                val name = user.user_metadata?.full_name
                    ?: user.email.substringBefore("@")

                binding.tvName.text = name
                binding.tvEmail.text = user.email

                // Avatar
                val avatarUrl = user.user_metadata?.avatar_url
                if (!avatarUrl.isNullOrEmpty()) {
                    Picasso.get()
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_profile)
                        .into(binding.ivAvatar)
                }
            }
        }

        viewModel.error.observe(this) { msg ->
            if (msg != null) {
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
