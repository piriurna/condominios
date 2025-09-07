package com.zalamena.condominios.moradores.app.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.zalamena.condominios.app.database.AppDatabase
import com.zalamena.condominios.moradores.data.dao.MoradoresDao
import com.zalamena.condominios.moradores.data.mapper.MoradorMapper
import com.zalamena.condominios.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.moradores.database.MoradoresDaoTest
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import org.koin.dsl.module

val testModule = module {
    single<AppDatabase> {
        // Use an in-memory database for testing
        Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries() // Simplify testing
            .build()
    }
    single<MoradoresDao> { get<AppDatabase>().getMoradoresDao() }
    single<MoradoresDao> { MoradoresDaoTest() }
    single<MoradoresRepository> { MoradoresRepositoryImpl(get(), MoradorMapper()) } // Use test-friendly implementations
}