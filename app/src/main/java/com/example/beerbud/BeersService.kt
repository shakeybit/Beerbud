package com.example.beerbud
import com.example.beerbud.models.Beer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BeersService {
    @GET("api/beers")
    fun getBeers(): Call<List<Beer>>

    @GET("beers/{beerId}")
    fun getBeerById(@Path("beerId") bookId: Int): Call<Beer>

    @POST("beers")
    fun saveBeer(@Body beer: Beer): Call<Beer>

    @DELETE("beers/{id}")
    fun deleteBeer(@Path("id") id: Int): Call<Beer>

    @PUT("beers/{id}")
    fun updateBeer(@Path("id") id: Int, @Body beer: Beer): Call<Beer>
}