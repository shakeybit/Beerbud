package com.example.beerbud


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beerbud.databinding.FragmentBeerListBinding
import com.example.beerbud.models.Beer
import com.example.beerbud.models.BeersAdapter
import com.example.beerbud.models.BeersViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class BeerListFragment : Fragment() {

    private var _binding: FragmentBeerListBinding? = null
    private val binding get() = _binding!!

    private val beersViewModel: BeersViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerListBinding.inflate(inflater, container, false)
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
                        findNavController().navigate(R.id.signInFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        beersViewModel.filteredBeerData.observe(viewLifecycleOwner, Observer { beers ->
            if (beers != null) {
                binding.recyclerView.adapter = BeersAdapter(beers, { beer ->
                    // Handle beer item click if necessary
                }, { beer ->
                    addToFavorite(beer)
                }, { beer ->
                    deleteBeer(beer)
                }, false) // Not a favorite list
            } else {
                // Handle empty or null data
            }
        })

        beersViewModel.fetchBeerData()

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val sortOption = parent.getItemAtPosition(position).toString()
                beersViewModel.sortBeers(sortOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val filterOption = parent.getItemAtPosition(position).toString()
                // Set the hint of filter input based on the selected option
                binding.filterInput.hint = "Enter $filterOption"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        binding.filterInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filterOption = binding.filterSpinner.selectedItem.toString()
                beersViewModel.filterBeers(filterOption, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun addToFavorite(beer: Beer) {
        val userId = auth.currentUser?.uid ?: return
        val ref = database.getReference("favorites").child(userId).child(beer.id.toString())
        ref.setValue(beer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Added to Favorites!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to add to Favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteBeer(beer: Beer) {
        beersViewModel.deleteBeer(beer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}