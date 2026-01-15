package com.example.brewappsassignment.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.FavoritesApi
import com.example.brewappsassignment.data.remote.QuotesApi
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.databinding.ActivityFavoritesBinding
import com.example.brewappsassignment.domain.repository.QuoteRepository

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: FavoriteAdapter

    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(
            QuoteRepository(
                SupabaseClient.instance.create(QuotesApi::class.java),
                SupabaseClient.instance.create(FavoritesApi::class.java),
                SessionManager(this)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        setupObservers()
        viewModel.loadFavorites()
    }

    private fun setupRecycler() {
        adapter = FavoriteAdapter()
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.favorites.observe(this) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}
