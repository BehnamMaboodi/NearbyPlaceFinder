package me.behna.nearbyplace.data


import com.google.gson.annotations.SerializedName

data class CenterModel(
    @SerializedName("latitude")
    val latitude: Double = 0.0,
    @SerializedName("longitude")
    val longitude: Double = 0.0
) : JsonModel()