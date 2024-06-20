package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.beerbud.databinding.FragmentBeerListBinding
import com.example.beerbud.models.BeersAdapter
import com.example.beerbud.models.BeersViewModel

class BeerListFragment : Fragment() {

    private var _binding: FragmentBeerListBinding? = null
    private val binding get() = _binding!!

    private val beersViewModel: BeersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beersViewModel.filteredBeerData.observe(viewLifecycleOwner, Observer { beers ->
            binding.recyclerView.adapter = BeersAdapter(beers) { beer ->
                // Handle beer item click, e.g., add to favorites
            }
        })

        beersViewModel.fetchBeerData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
