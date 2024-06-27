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
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.beerbud.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
                    R.id.action_favorite_beers -> {
                        findNavController().navigate(R.id.favoriteBeersFragment)
                        true
                    }
                    R.id.action_add_beer -> {
                        findNavController().navigate(R.id.addBeerFragment)
                        true
                    }
                    R.id.action_sign_in -> {
                        auth.signOut()
                        findNavController().navigate(R.id.signInFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Display user email in the welcome message
        val userEmail = auth.currentUser?.email
        binding.welcomeMessage.text = "Welcome back, ${userEmail ?: "user"}"

        // Set up button navigation
        binding.buttonBeerList.setOnClickListener {
            findNavController().navigate(R.id.beerListFragment)
        }
        binding.buttonFavoriteBeers.setOnClickListener {
            findNavController().navigate(R.id.favoriteBeersFragment)
        }
        binding.buttonAddBeer.setOnClickListener {
            findNavController().navigate(R.id.addBeerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}