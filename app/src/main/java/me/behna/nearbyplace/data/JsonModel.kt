package me.behna.nearbyplace.data

import com.google.gson.Gson

open class JsonModel {
    open fun toJson(): String {
        return Gson().toJson(this)
    }
}