package com.example.brewappsassignment.ui.favorites

import androidx.recyclerview.widget.RecyclerView
import com.example.brewappsassignment.data.remote.models.QuoteDto
import com.example.brewappsassignment.databinding.ItemQuoteBinding

class FavoriteViewHolder(private val binding: ItemQuoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: QuoteDto) {
        binding.tvQuote.text = "“${item.text}”"
        binding.tvAuthor.text = item.author
        binding.tvCategory.text = item.category.uppercase()

        // Hide favorite button in Favorites screen
        binding.ivFav.setImageResource(0)
    }
}
