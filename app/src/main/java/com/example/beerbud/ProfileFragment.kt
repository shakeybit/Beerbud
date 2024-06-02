package com.example.beerbud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var welcomeTextView: TextView
    private lateinit var signOutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        signOutButton = view.findViewById(R.id.signOutButton)

        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser)

        signOutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(activity, "Signed out", Toast.LENGTH_SHORT).show()
            // Navigate to RegisterFragment or another appropriate fragment
            findNavController().navigate(R.id.action_profileFragment_to_registerFragment)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val email = user.email
            welcomeTextView.text = "Welcome back, $email"
        } else {
            welcomeTextView.text = "Welcome back, User"
        }
    }
}