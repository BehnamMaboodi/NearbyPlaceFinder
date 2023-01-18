package me.behna.nearbyplace.data.model

import com.google.gson.Gson

open class JsonModel {
    open fun toJson(): String {
        return Gson().toJson(this)
    }
}