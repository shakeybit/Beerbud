package com.example.beerbud.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beerbud.databinding.ItemFavoriteBeerBinding
import com.example.beerbud.models.Beer

class FavoriteBeersAdapter(
    private val beers: List<Beer>,
    private val onRemoveClick: (Beer) -> Unit
) : RecyclerView.Adapter<FavoriteBeersAdapter.BeerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = ItemFavoriteBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(beers[position], onRemoveClick)
    }

    override fun getItemCount(): Int = beers.size

    class BeerViewHolder(private val binding: ItemFavoriteBeerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer, onRemoveClick: (Beer) -> Unit) {
            binding.apply {
                this.beer = beer
                removeFromFavoriteButton.setOnClickListener { onRemoveClick(beer) }
                executePendingBindings()
            }
        }
    }
}