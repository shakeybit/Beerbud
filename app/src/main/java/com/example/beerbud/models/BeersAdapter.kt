package com.example.beerbud.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beerbud.databinding.ItemBeerBinding
import com.example.beerbud.databinding.ItemFavoriteBeerBinding

class BeersAdapter(
    private val beers: List<Beer>,
    private val onClick: (Beer) -> Unit,
    private val onFavoriteClick: (Beer) -> Unit,
    private val onDeleteClick: (Beer) -> Unit, // New parameter for delete action
    private val isFavoriteList: Boolean // New parameter to differentiate lists
) : RecyclerView.Adapter<BeersAdapter.BeerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding, isFavoriteList)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(beers[position], onClick, onFavoriteClick, onDeleteClick)
    }

    override fun getItemCount(): Int = beers.size

    class BeerViewHolder(
        private val binding: ItemBeerBinding,
        private val isFavoriteList: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer, onClick: (Beer) -> Unit, onFavoriteClick: (Beer) -> Unit, onDeleteClick: (Beer) -> Unit) {
            binding.apply {
                this.beer = beer
                root.setOnClickListener { onClick(beer) }
                addToFavoriteButton.setOnClickListener { onFavoriteClick(beer) }

                // Show delete button only for user-added beers if not in favorite list
                if (!isFavoriteList && beer.user.isNotEmpty()) {
                    deleteBeerButton.visibility = View.VISIBLE
                    deleteBeerButton.setOnClickListener { onDeleteClick(beer) }
                } else {
                    deleteBeerButton.visibility = View.GONE
                }

                // Hide the add to favorite button if it's a favorite list
                addToFavoriteButton.visibility = if (isFavoriteList) View.GONE else View.VISIBLE

                executePendingBindings()
            }
        }
    }
}