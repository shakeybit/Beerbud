package com.example.beerbud.repository

import com.example.beerbud.models.Beer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("api/beers")
    suspend fun getBeers(): List<Beer>
}

object ApiServer {
    private const val BASE_URL = "https://anbo-restbeer.azurewebsites.net/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}