package com.example.beerbud.models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerbud.repository.BeersRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    private val _beerData = MutableLiveData<List<Beer>>()
    val beerData: LiveData<List<Beer>> get() = _beerData

    private val _filteredBeerData = MutableLiveData<List<Beer>>()
    val filteredBeerData: LiveData<List<Beer>> get() = _filteredBeerData

    private val database = FirebaseDatabase.getInstance()
    private val repository = BeersRepository()

    init {
        fetchAndStoreApiBeers()
        fetchBeerData()
    }

    private fun fetchAndStoreApiBeers() {
        viewModelScope.launch {
            repository.fetchAndStoreBeers()
        }
    }

    fun fetchBeerData() {
        viewModelScope.launch {
            try {
                val apiRef = database.getReference("apiBeers")
                val userRef = database.getReference("userBeers")

                val combinedBeers = mutableListOf<Beer>()

                apiRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        combinedBeers.clear()
                        for (beerSnapshot in snapshot.children) {
                            val beer = beerSnapshot.getValue(Beer::class.java)
                            if (beer != null) {
                                combinedBeers.add(beer)
                            }
                        }

                        // Fetch user-added beers after API beers
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                for (beerSnapshot in userSnapshot.children) {
                                    val beer = beerSnapshot.getValue(Beer::class.java)
                                    if (beer != null) {
                                        combinedBeers.add(beer)
                                    }
                                }
                                _beerData.postValue(combinedBeers)
                                _filteredBeerData.postValue(combinedBeers)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("BeersViewModel", "Error fetching user beers", error.toException())
                            }
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("BeersViewModel", "Error fetching API beers", error.toException())
                    }
                })
            } catch (e: Exception) {
                Log.e("BeersViewModel", "Error fetching beers", e)
            }
        }
    }

    fun addBeer(beer: Beer) {
        val ref = database.getReference("userBeers").push()
        beer.id = ref.key?.hashCode() ?: 0
        ref.setValue(beer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fetchBeerData() // Refresh beer list after adding a new beer
            } else {
                Log.e("BeersViewModel", "Error adding beer: ${task.exception?.message}")
            }
        }
    }

    fun deleteBeer(beer: Beer) {
        val ref = database.getReference("userBeers").orderByChild("id").equalTo(beer.id.toDouble())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (beerSnapshot in snapshot.children) {
                    beerSnapshot.ref.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            fetchBeerData() // Refresh beer list after deletion
                        } else {
                            Log.e("BeersViewModel", "Error deleting beer: ${task.exception?.message}")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BeersViewModel", "Error deleting beer", error.toException())
            }
        })
    }

    fun sortBeers(sortOption: String) {
        _filteredBeerData.value?.let { beers ->
            val sortedBeers = when (sortOption) {
                "Name" -> beers.sortedBy { it.name }
                "Brewery" -> beers.sortedBy { it.brewery }
                "Style" -> beers.sortedBy { it.style }
                "ABV" -> beers.sortedBy { it.abv }
                "Volume" -> beers.sortedBy { it.volume }
                "Amount" -> beers.sortedBy { it.howMany }
                else -> beers
            }
            _filteredBeerData.postValue(sortedBeers)
        }
    }

    fun filterBeers(filterOption: String, filterValue: String) {
        _beerData.value?.let { beers ->
            val filteredBeers = when (filterOption) {
                "Name" -> beers.filter { it.name.contains(filterValue, ignoreCase = true) }
                "Brewery" -> beers.filter { it.brewery.contains(filterValue, ignoreCase = true) }
                "Style" -> beers.filter { it.style.contains(filterValue, ignoreCase = true) }
                "ABV" -> beers.filter { it.abv.toString().contains(filterValue, ignoreCase = true) }
                "Volume" -> beers.filter { it.volume.toString().contains(filterValue, ignoreCase = true) }
                "Amount" -> beers.filter { it.howMany.toString().contains(filterValue, ignoreCase = true) }
                else -> beers
            }
            _filteredBeerData.postValue(filteredBeers)
        }
    }
}