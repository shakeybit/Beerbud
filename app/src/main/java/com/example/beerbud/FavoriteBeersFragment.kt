package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.beerbud.databinding.FragmentFavoriteBeersBinding
import com.example.beerbud.models.BeersAdapter

class FavoriteBeersFragment : Fragment() {

    private var _binding: FragmentFavoriteBeersBinding? = null
    private val binding get() = _binding!!

    private val favoriteBeersViewModel: FavoriteBeersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBeersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteBeersViewModel.favoriteBeers.observe(viewLifecycleOwner, Observer { beers ->
            binding.recyclerView.adapter = BeersAdapter(beers) { beer ->
                // Handle beer item click, e.g., remove from favorites
            }
        })

        favoriteBeersViewModel.fetchFavoriteBeers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
