package me.behna.nearbyplace.data


import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("description")
    val description: String = ""
) : JsonModel()