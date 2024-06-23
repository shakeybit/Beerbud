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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        binding.welcomeTextView.text = "Welcome back, ${user?.email}"

        binding.beerListButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_beerListFragment)
        }

        binding.favoriteBeersButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoriteBeersFragment)
        }

        binding.signOutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
        }

        // Set up MenuProvider
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
                    R.id.action_sign_in -> {
                        findNavController().navigate(R.id.signInFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}