package com.example.beerbud.repository

import com.example.beerbud.models.Beer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BeersRepository {

    private val api: ApiServer

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://anbo-restbeer.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiServer::class.java)
    }

    suspend fun getBeers(): List<Beer> {
        return api.getBeers()
    }
}