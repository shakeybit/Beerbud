package com.example.beerbud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerbud.models.Beer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class FavoriteBeersViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    private val _favoriteBeers = MutableLiveData<List<Beer>>()
    val favoriteBeers: LiveData<List<Beer>> get() = _favoriteBeers

    init {
        fetchFavoriteBeers()
    }

    fun fetchFavoriteBeers() {
        viewModelScope.launch {
            try {
                user?.let {
                    firestore.collection("users")
                        .document(it.uid)
                        .collection("favorites")
                        .get()
                        .addOnSuccessListener { result ->
                            val beers = result.map { document ->
                                document.toObject(Beer::class.java)
                            }
                            _favoriteBeers.postValue(beers)
                        }
                        .addOnFailureListener {
                            // Handle error
                        }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addBeerToFavorites(beer: Beer) {
        user?.let {
            firestore.collection("users")
                .document(it.uid)
                .collection("favorites")
                .document(beer.id.toString())
                .set(beer)
                .addOnSuccessListener {
                    fetchFavoriteBeers()
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    fun removeBeerFromFavorites(beer: Beer) {
        user?.let {
            firestore.collection("users")
                .document(it.uid)
                .collection("favorites")
                .document(beer.id.toString())
                .delete()
                .addOnSuccessListener {
                    fetchFavoriteBeers()
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }
}
