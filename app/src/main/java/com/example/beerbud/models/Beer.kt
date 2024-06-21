package com.example.beerbud.models

data class Beer(
    val id: Int,
    val user: String,
    val brewery: String,
    val name: String,
    val style: String,
    val abv: Double,
    val volume: Double,
    val pictureUrl: String,
    val howMany: Int
)

