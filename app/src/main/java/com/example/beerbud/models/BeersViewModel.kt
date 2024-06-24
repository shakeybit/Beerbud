package com.example.beerbud.models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerbud.repository.BeersRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    private val repository = BeersRepository()

    private val _beerData = MutableLiveData<List<Beer>>()
    val beerData: LiveData<List<Beer>> get() = _beerData

    init {
        fetchBeerData()
    }

    fun fetchBeerData() {
        viewModelScope.launch {
            try {
                val beers = repository.getBeers()
                _beerData.postValue(beers)
                Log.d("BeersViewModel", "Fetched ${beers.size} beers")
            } catch (e: Exception) {
                Log.e("BeersViewModel", "Error fetching beers", e)
            }
        }
    }
}