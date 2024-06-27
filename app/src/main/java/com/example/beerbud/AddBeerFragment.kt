package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.beerbud.databinding.FragmentAddBeerBinding
import com.example.beerbud.models.Beer
import com.example.beerbud.models.BeersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AddBeerFragment : Fragment() {

    private var _binding: FragmentAddBeerBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBeerBinding.inflate(inflater, container, false)
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

                    R.id.action_sign_in -> {
                        findNavController().navigate(R.id.signInFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.addBeerButton.setOnClickListener {
            val name = binding.beerNameInput.text.toString()
            val brewery = binding.beerBreweryInput.text.toString()
            val style = binding.beerStyleInput.text.toString()
            val abv = binding.beerAbvInput.text.toString().toDoubleOrNull()
            val volume = binding.beerVolumeInput.text.toString().toDoubleOrNull()
            val howMany = binding.beerAmountInput.text.toString().toIntOrNull()

            if (name.isNotEmpty() && brewery.isNotEmpty() && style.isNotEmpty() && abv != null && volume != null && howMany != null) {
                val user = auth.currentUser?.email ?: ""
                val beer = Beer(0, user, brewery, name, style, abv, volume, "", howMany)
                addBeer(beer)
            } else {
                // Show error message
            }
        }
    }

    private fun addBeer(beer: Beer) {
        val ref = database.getReference("userBeers").push()
        beer.id = ref.key?.hashCode() ?: 0
        ref.setValue(beer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Navigate back to beer list or show success message
            } else {
                // Show error message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}