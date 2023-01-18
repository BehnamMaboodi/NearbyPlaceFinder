package me.behna.nearbyplace.data


import com.google.gson.annotations.SerializedName

data class BusinessSearchResultModel(
    @SerializedName("businesses")
    val businesses: List<BusinessModel> = listOf(),
    @SerializedName("region")
    val region: RegionModel = RegionModel(),
    @SerializedName("total")
    val total: Int = 0
) : JsonModel()