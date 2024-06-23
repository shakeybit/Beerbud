package com.example.beerbud.models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    private val _beerData = MutableLiveData<List<Beer>>()
    val beerData: LiveData<List<Beer>> get() = _beerData

    private val database = FirebaseDatabase.getInstance()

    init {
        fetchBeerData()
    }

    fun fetchBeerData() {
        viewModelScope.launch {
            try {
                val ref = database.getReference("beers")
                ref.get().addOnSuccessListener { snapshot ->
                    val beers = snapshot.children.mapNotNull { it.getValue(Beer::class.java) }
                    _beerData.postValue(beers)
                }.addOnFailureListener { exception ->
                    Log.e("BeersViewModel", "Error fetching beers", exception)
                }
            } catch (e: Exception) {
                Log.e("BeersViewModel", "Error fetching beers", e)
            }
        }
    }
    fun addBeer(beer: Beer) {
        val ref = database.getReference("beers").push()
        beer.id = ref.key?.hashCode() ?: 0
        ref.setValue(beer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fetchBeerData() // Refresh beer list after adding a new beer
            } else {
                Log.e("BeersViewModel", "Error adding beer: ${task.exception?.message}")
            }
        }
    }
}
