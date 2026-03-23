package com.zalamena.condominios.di

import org.koin.core.context.startKoin

actual object PlatformKoinInitializer {
    actual fun init() {
        startKoin {
            modules(
                platformModule,
                networkModule,
                daoModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}