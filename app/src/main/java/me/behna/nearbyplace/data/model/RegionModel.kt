package me.behna.nearbyplace.data.model


import com.google.gson.annotations.SerializedName

data class RegionModel(
    @SerializedName("center")
    val center: CenterModel = CenterModel()
) : JsonModel()