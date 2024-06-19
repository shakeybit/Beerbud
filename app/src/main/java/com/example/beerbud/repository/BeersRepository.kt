package com.example.beerbud.repository

import com.example.beerbud.models.Beer

class BeersRepository {

    private val apiService = ApiServer.apiService

    suspend fun getBeers(): List<Beer> {
        return apiService.getBeers()
    }
}