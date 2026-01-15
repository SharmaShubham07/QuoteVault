package com.example.brewappsassignment.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brewappsassignment.databinding.ActivityQuoteDetailsBinding

class QuoteDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quote = intent.getStringExtra("quote")
        val author = intent.getStringExtra("author")
        val category = intent.getStringExtra("category")

        binding.tvQuote.text = quote
        binding.tvAuthor.text = author
        binding.tvCategory.text = category
    }
}
