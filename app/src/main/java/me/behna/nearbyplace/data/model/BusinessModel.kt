package me.behna.nearbyplace.data.model


import com.google.gson.annotations.SerializedName

data class BusinessModel(
    @SerializedName("alias")
    val alias: String = "",
    @SerializedName("categories")
    val categories: List<CategoryModel> = listOf(),
    @SerializedName("coordinates")
    val coordinates: CoordinatesModel = CoordinatesModel(),
    @SerializedName("display_phone")
    val displayPhone: String = "",
    @SerializedName("distance")
    val distance: Double = 0.0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("is_closed")
    val isClosed: Boolean = false,
    @SerializedName("location")
    val location: LocationModel = LocationModel(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("rating")
    val rating: Double = 0.0,
    @SerializedName("review_count")
    val reviewCount: Int = 0,
    @SerializedName("transactions")
    val transactions: List<String> = listOf(),
    @SerializedName("url")
    val url: String = ""
) : JsonModel() {


    fun getCategoryNames(): List<String> = categories.map { it.title }

}