package com.example.beerbud.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beerbud.databinding.ItemBeerBinding

class BeersAdapter(
    private val beers: List<Beer>,
    private val onClick: (Beer) -> Unit,
    private val onFavoriteClick: (Beer) -> Unit,
    private val onRemoveClick: (Beer) -> Unit // Add this parameter
) : RecyclerView.Adapter<BeersAdapter.BeerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(beers[position], onClick, onFavoriteClick, onRemoveClick)
    }

    override fun getItemCount(): Int = beers.size

    class BeerViewHolder(private val binding: ItemBeerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer, onClick: (Beer) -> Unit, onFavoriteClick: (Beer) -> Unit, onRemoveClick: (Beer) -> Unit) {
            binding.apply {
                this.beer = beer
                root.setOnClickListener { onClick(beer) }
                addToFavoriteButton.setOnClickListener { onFavoriteClick(beer) }
                root.setOnLongClickListener {
                    onRemoveClick(beer)
                    true
                }
            }
        }
    }
}