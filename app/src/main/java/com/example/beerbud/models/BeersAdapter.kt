package com.example.beerbud.models
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beerbud.databinding.ItemBeerBinding

class BeersAdapter(private val beers: List<Beer>, private val onClick: (Beer) -> Unit) : RecyclerView.Adapter<BeersAdapter.BeerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(beers[position], onClick)
    }

    override fun getItemCount(): Int = beers.size

    class BeerViewHolder(private val binding: ItemBeerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer, onClick: (Beer) -> Unit) {
            binding.apply {
                beerName.text = beer.name
                beerBrewery.text = beer.brewery
                beerStyle.text = beer.style
                beerAbv.text = "ABV: ${beer.abv}%"

                root.setOnClickListener { onClick(beer) }
            }
        }
    }
}