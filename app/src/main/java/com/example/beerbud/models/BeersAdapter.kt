package com.example.beerbud.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beerbud.databinding.ItemBeerBinding
import com.example.beerbud.databinding.ItemFavoriteBeerBinding

class BeersAdapter(
    private val beers: List<Beer>,
    private val onClick: (Beer) -> Unit,
    private val onFavoriteClick: (Beer) -> Unit,
    private val onRemoveClick: (Beer) -> Unit,
    private val isFavoriteList: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isFavoriteList) {
            val binding = ItemFavoriteBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FavoriteBeerViewHolder(binding)
        } else {
            val binding = ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BeerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isFavoriteList) {
            (holder as FavoriteBeerViewHolder).bind(beers[position], onClick, onRemoveClick)
        } else {
            (holder as BeerViewHolder).bind(beers[position], onClick, onFavoriteClick)
        }
    }

    override fun getItemCount(): Int = beers.size

    class BeerViewHolder(private val binding: ItemBeerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer, onClick: (Beer) -> Unit, onFavoriteClick: (Beer) -> Unit) {
            binding.apply {
                this.beer = beer
                root.setOnClickListener { onClick(beer) }
                addToFavoriteButton.setOnClickListener { onFavoriteClick(beer) }
                executePendingBindings()
            }
        }
    }

    class FavoriteBeerViewHolder(private val binding: ItemFavoriteBeerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer, onClick: (Beer) -> Unit, onRemoveClick: (Beer) -> Unit) {
            binding.apply {
                this.beer = beer
                root.setOnClickListener { onClick(beer) }
                removeFromFavoriteButton.setOnClickListener { onRemoveClick(beer) }
                executePendingBindings()
            }
        }
    }
}