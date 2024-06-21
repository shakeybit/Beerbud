package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
