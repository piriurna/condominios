package com.zalamena.condominios

import android.app.Application
import com.zalamena.condominios.di.PlatformKoinInitializer

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Set the context for Koin
        PlatformKoinInitializer.setContext(this)

        // Initialize Koin
        PlatformKoinInitializer.init()
    }
}