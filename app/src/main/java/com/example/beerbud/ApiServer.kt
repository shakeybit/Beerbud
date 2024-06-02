package com.example.beerbud
import retrofit2.Call
import retrofit2.http.GET
interface ApiServer {
    @GET("api/beers")
    fun getBeers(): Call<List<Beer>>
}