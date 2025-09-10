package com.zalamena.condominios.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

actual object PlatformKoinInitializer {
    private lateinit var applicationContext: Context

    fun setContext(context: Context) {
        applicationContext = context
    }
    actual fun init() {
        startKoin {
            androidContext(applicationContext)
            modules(
                platformModule(),
                daoModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}