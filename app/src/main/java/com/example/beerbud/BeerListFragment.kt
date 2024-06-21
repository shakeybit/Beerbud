package com.example.beerbud


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        beersViewModel.beerData.observe(viewLifecycleOwner, Observer { beers ->
            if (beers != null) {
                binding.recyclerView.adapter = BeersAdapter(beers, { beer ->
                    // Handle beer item click if necessary
                }, { beer ->
                    addToFavorite(beer)
                }, { beer ->
                    // No remove functionality here
                })
            } else {
                // Handle empty or null data
            }
        })

        beersViewModel.fetchBeerData()
    }

    private fun addToFavorite(beer: Beer) {
        val userId = auth.currentUser?.uid ?: return
        val ref = database.getReference("favorites").child(userId).child(beer.id.toString())
        ref.setValue(beer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}