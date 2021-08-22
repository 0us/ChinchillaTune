package com.example.chinchillatuner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate() // The foo field is injected in super.onCreate()
        Timber.plant(
            Timber.DebugTree()
        )
    }
}