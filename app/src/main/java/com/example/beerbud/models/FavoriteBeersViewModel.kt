package com.example.beerbud.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.firebase.database.*

class FavoriteBeersViewModel : ViewModel() {

    private val _favoriteBeers = MutableLiveData<List<Beer>>()
    val favoriteBeers: LiveData<List<Beer>> get() = _favoriteBeers

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    init {
        fetchFavoriteBeers()
    }

    private fun fetchFavoriteBeers() {
        val userId = auth.currentUser?.uid ?: return
        val ref = database.getReference("favorites").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val beers = mutableListOf<Beer>()
                for (beerSnapshot in snapshot.children) {
                    val beer = beerSnapshot.getValue(Beer::class.java)
                    if (beer != null) {
                        beers.add(beer)
                    }
                }
                _favoriteBeers.postValue(beers)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}