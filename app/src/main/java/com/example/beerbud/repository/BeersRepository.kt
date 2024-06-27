package com.example.beerbud.repository

import android.util.Log
import com.example.beerbud.models.Beer
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BeersApi {
    @GET("beers")
    suspend fun getBeers(): List<Beer>
}

class BeersRepository {

    private val api: BeersApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://anbo-restbeer.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(BeersApi::class.java)
    }

    suspend fun fetchAndStoreBeers() {
        try {
            val beers = withContext(Dispatchers.IO) { api.getBeers() }
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("apiBeers")

            ref.setValue(beers).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("BeersRepository", "Beers fetched and stored successfully")
                } else {
                    Log.e("BeersRepository", "Error storing beers: ${task.exception?.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("BeersRepository", "Error fetching beers", e)
        }
    }
}