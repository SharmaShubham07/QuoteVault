package com.example.brewappsassignment.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brewappsassignment.R
import com.example.brewappsassignment.data.remote.models.QuoteDto
import com.example.brewappsassignment.databinding.ItemQuoteBinding

class QuoteAdapter(
    private val onFavClick: (QuoteDto) -> Unit,
    private val onShareCard: (QuoteDto) -> Unit
) : RecyclerView.Adapter<QuoteViewHolder>() {

    private val list = mutableListOf<QuoteDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuoteViewHolder(binding, onFavClick, onShareCard)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun submitList(newList: List<QuoteDto>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}

class QuoteViewHolder(
    private val binding: ItemQuoteBinding,
    private val onFavClick: (QuoteDto) -> Unit,
    private val onShareCard: (QuoteDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: QuoteDto) {

        val context = binding.root.context

        binding.tvQuote.text = context.getString(R.string.quote_text_format, item.text)
        binding.tvAuthor.text = item.author
        binding.tvCategory.text = item.category.uppercase()

        // Favorite icon logic
        binding.ivFav.setImageResource(
            if (item.isFavorite) R.drawable.ic_heart_filled
            else R.drawable.ic_heart_outline
        )

        binding.ivFav.setOnClickListener {
            item.isFavorite = !item.isFavorite

            binding.ivFav.setImageResource(
                if (item.isFavorite) R.drawable.ic_heart_filled
                else R.drawable.ic_heart_outline
            )

            onFavClick(item)
        }

        // Share card click
        binding.btnShareCard.setOnClickListener {
            onShareCard(item)
        }
    }
}
