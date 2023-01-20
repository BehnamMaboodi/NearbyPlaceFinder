package me.behna.nearbyplace.utilities

object TextUtils {
    fun joinToString(list: List<String>, limit: Int): String {
        return list.joinToString(limit = limit)
    }
}