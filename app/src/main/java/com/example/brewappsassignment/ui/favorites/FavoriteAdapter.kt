package com.example.brewappsassignment.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brewappsassignment.data.remote.models.QuoteDto
import com.example.brewappsassignment.databinding.ItemQuoteBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteViewHolder>() {

    private val list = mutableListOf<QuoteDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemQuoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submitList(newList: List<QuoteDto>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
