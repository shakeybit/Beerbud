package com.example.beerbud.repository

import com.example.beerbud.models.Beer
import retrofit2.http.GET

interface ApiServer {
    @GET("beers")
    suspend fun getBeers(): List<Beer>
}