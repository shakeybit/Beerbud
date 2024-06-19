package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.beerbud.databinding.FragmentHomeBinding
import com.example.beerbud.models.BeersAdapter
import com.example.beerbud.models.BeersViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val beersViewModel: BeersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beersViewModel.filteredBeerData.observe(viewLifecycleOwner, Observer { beers ->
            // Update UI with filtered or sorted beer data
            binding.recyclerView.adapter = BeersAdapter(beers) { beer ->
                // Handle beer item click
            }
        })

        beersViewModel.fetchBeerData()

        // Example of filtering and sorting
        binding.filterButton.setOnClickListener {
            beersViewModel.filterBeers("Grøn")
        }

        binding.sortButton.setOnClickListener {
            beersViewModel.sortBeersByAbv()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}