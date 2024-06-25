package com.example.beerbud.models

import com.google.gson.annotations.SerializedName

data class Beer(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("user") val user: String = "",
    @SerializedName("brewery") val brewery: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("style") val style: String = "",
    @SerializedName("abv") val abv: Double = 0.0,
    @SerializedName("volume") val volume: Double = 0.0,
    @SerializedName("pictureUrl") val pictureUrl: String? = null,
    @SerializedName("howMany") val howMany: Int = 0
) {
    // No-argument constructor for Firebase
    constructor() : this(0, "", "", "", "", 0.0, 0.0, null, 0)
}