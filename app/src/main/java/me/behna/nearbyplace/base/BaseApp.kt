package me.behna.nearbyplace.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.behna.nearbyplace.BuildConfig
import timber.log.Timber
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeTimber()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) plant(Timber.DebugTree())
    }
}