package com.example.beerbud.models

data class Beer(
    var id: Int,
    val user: String,
    val brewery: String,
    val name: String,
    val style: String,
    val abv: Double,
    val volume: Double,
    val pictureUrl: String?,
    val howMany: Int
){
    // No-argument constructor for firebase
    constructor() : this(0, "", "", "", "", 0.0, 0.0, null, 0)
}

