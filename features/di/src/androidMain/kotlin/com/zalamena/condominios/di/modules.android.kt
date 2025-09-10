package com.zalamena.condominios.di

import com.zalamena.condominios.database.AppDatabase
import com.zalamena.condominios.database.getDatabaseBuilder
import com.zalamena.condominios.database.getRoomDatabase
import org.koin.dsl.module

actual val platformModule = module {
    single<AppDatabase> { getRoomDatabase(getDatabaseBuilder(get())) }
}