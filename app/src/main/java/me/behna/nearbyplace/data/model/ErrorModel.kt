package me.behna.nearbyplace.data.model


import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("code")
    val code: String = UNKNOWN_ERROR,
    @SerializedName("description")
    val description: String = "Unknown Error"
) : Throwable() {

    companion object {
        const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
        const val VALIDATION_ERROR = "VALIDATION_ERROR"
        const val LOCATION_NOT_FOUND_ERROR = "LOCATION_NOT_FOUND"
    }
}