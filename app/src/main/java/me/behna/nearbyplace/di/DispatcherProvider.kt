package me.behna.nearbyplace.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

object DispatcherProvider {
    private var isTest: Boolean? = null

    operator fun invoke(isTest: Boolean? = null): DispatcherProvider {
        this.isTest = isTest
        return this
    }

    val Main: CoroutineDispatcher
        get() = getTestDispatcher(isTest) ?: Dispatchers.Main

    val IO: CoroutineDispatcher
        get() = getTestDispatcher(isTest) ?: Dispatchers.IO

    val Default: CoroutineDispatcher
        get() = getTestDispatcher(isTest) ?: Dispatchers.Default

    val Unconfined: CoroutineDispatcher
        get() = getTestDispatcher(isTest) ?: Dispatchers.Unconfined


    private fun getTestDispatcher(isTest: Boolean?): CoroutineDispatcher? =
        if (isTest == true) testDispatcher else null

    private val testDispatcher: CoroutineDispatcher by lazy {
        newSingleThreadContext("Test Dispatcher")
    }

}