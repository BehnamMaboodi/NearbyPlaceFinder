package me.behna.nearbyplace.data.model


import com.google.gson.annotations.SerializedName

data class ErrorResultModel(
    @SerializedName("error")
    val error: ErrorModel = ErrorModel()
) : Throwable()