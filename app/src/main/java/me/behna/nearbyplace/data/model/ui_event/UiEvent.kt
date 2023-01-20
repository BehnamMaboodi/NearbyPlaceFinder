package me.behna.nearbyplace.data.model.ui_event

import me.behna.nearbyplace.R

sealed interface UiEvent<T : Any?> {
    val messageStringRes: Int?
    val message: String?
    val data: T?

    class Success<T>(
        override val messageStringRes: Int? = R.string.success,
        override val message: String? = "Success", override val data: T? = null
    ) : UiEvent<T>

    interface Error<T> : UiEvent<T>

    class InvalidInput<T>(
        override val messageStringRes: Int? = R.string.invalid_input,
        override val message: String? = "Invalid Input", override val data: T? = null
    ) : Error<T>

    class ServerError<T>(
        override val messageStringRes: Int? = R.string.server_error,
        override val message: String? = "Server Error", override val data: T? = null
    ) : Error<T>

    class NetworkError<T>(
        override val messageStringRes: Int? = R.string.network_error,
        override val message: String? = "Network Error", override val data: T? = null
    ) : Error<T>

    class Refresh<T>(
        override val messageStringRes: Int? = R.string.unknown_error,
        override val message: String? = "Unknown Error", override val data: T? = null
    ) : Error<T>

    class Other<T>(
        override val messageStringRes: Int? = R.string.unknown_error,
        override val message: String? = "Unknown Error", override val data: T? = null
    ) : Error<T>

}