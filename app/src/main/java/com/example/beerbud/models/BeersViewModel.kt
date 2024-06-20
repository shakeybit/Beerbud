package com.example.beerbud.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerbud.repository.BeersRepository
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    private val repository = BeersRepository()

    private val _beerData = MutableLiveData<List<Beer>>()
    val beerData: LiveData<List<Beer>> get() = _beerData

    private val _filteredBeerData = MutableLiveData<List<Beer>>()
    val filteredBeerData: LiveData<List<Beer>> get() = _filteredBeerData

    init {
        fetchBeerData()
    }

    fun fetchBeerData() {
        viewModelScope.launch {
            try {
                val beers = repository.getBeers()
                _beerData.postValue(beers)
                _filteredBeerData.postValue(beers) // Initialize filtered data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun filterBeers(criteria: String) {
        _beerData.value?.let { beers ->
            val filteredBeers = beers.filter { it.name.contains(criteria, ignoreCase = true) }
            _filteredBeerData.postValue(filteredBeers)
        }
    }

    fun sortBeersByAbv() {
        _filteredBeerData.value?.let { beers ->
            val sortedBeers = beers.sortedBy { it.abv }
            _filteredBeerData.postValue(sortedBeers)
        }
    }
}