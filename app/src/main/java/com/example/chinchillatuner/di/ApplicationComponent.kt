package com.example.chinchillatuner.di

import android.app.Application
import dagger.Component

// Definition of the Application graph
@Component
interface ApplicationComponent {  }

// appComponent lives in the Application class to share its lifecycle
class MyApplication: Application() {
    // Reference to the application graph that is used across the whole app
    val appComponent = DaggerApplicationComponent.create()
}
