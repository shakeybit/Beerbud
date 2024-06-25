package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.beerbud.databinding.FragmentAddBeerBinding
import com.example.beerbud.models.Beer
import com.example.beerbud.models.BeersViewModel
import com.google.firebase.auth.FirebaseAuth

class AddBeerFragment : Fragment() {

    private var _binding: FragmentAddBeerBinding? = null
    private val binding get() = _binding!!

    private val beersViewModel: BeersViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addBeerButton.setOnClickListener {
            val name = binding.beerNameInput.text.toString()
            val brewery = binding.beerBreweryInput.text.toString()
            val style = binding.beerStyleInput.text.toString()
            val abv = binding.beerAbvInput.text.toString().toDoubleOrNull()
            val volume = binding.beerVolumeInput.text.toString().toDoubleOrNull()
            val amount = binding.beerAmountInput.text.toString().toIntOrNull()
            val user = auth.currentUser?.email ?: ""

            if (name.isNotBlank() && brewery.isNotBlank() && style.isNotBlank() && abv != null && volume != null && amount != null) {
                val beer = Beer(id = 0, user = user, brewery = brewery, name = name, style = style, abv = abv, volume = volume, pictureUrl = null, howMany = amount)
                beersViewModel.addBeer(beer)
                Toast.makeText(context, "Beer added!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}