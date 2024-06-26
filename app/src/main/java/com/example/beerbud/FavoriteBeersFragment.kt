package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beerbud.databinding.FragmentFavoriteBeersBinding
import com.example.beerbud.models.Beer
import com.example.beerbud.models.BeersAdapter
import com.example.beerbud.models.FavoriteBeersAdapter
import com.example.beerbud.models.FavoriteBeersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
class FavoriteBeersFragment : Fragment() {

    private var _binding: FragmentFavoriteBeersBinding? = null
    private val binding get() = _binding!!

    private val favoriteBeersViewModel: FavoriteBeersViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBeersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        favoriteBeersViewModel.favoriteBeers.observe(viewLifecycleOwner, Observer { beers ->
            if (beers != null) {
                binding.recyclerView.adapter = FavoriteBeersAdapter(beers) { beer ->
                    removeFromFavorites(beer)
                }
            } else {
                // Handle empty or null data
            }
        })

        favoriteBeersViewModel.fetchFavoriteBeers()
    }

    private fun removeFromFavorites(beer: Beer) {
        favoriteBeersViewModel.removeFromFavorites(beer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}