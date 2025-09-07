package com.zalamena.condominios.app.di

import com.zalamena.condominios.app.database.AppDatabase
import com.zalamena.condominios.app.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder() }
}