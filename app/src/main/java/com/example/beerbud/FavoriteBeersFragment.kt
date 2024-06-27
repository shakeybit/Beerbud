package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beerbud.databinding.FragmentFavoriteBeersBinding
import com.example.beerbud.models.Beer
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

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_home -> {
                        findNavController().navigate(R.id.homeFragment)
                        true
                    }
                    R.id.action_beer_list -> {
                        findNavController().navigate(R.id.beerListFragment)
                        true
                    }

                    R.id.action_add_beer -> {
                        findNavController().navigate(R.id.addBeerFragment)
                        true
                    }
                    R.id.action_sign_in -> {
                        findNavController().navigate(R.id.signInFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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