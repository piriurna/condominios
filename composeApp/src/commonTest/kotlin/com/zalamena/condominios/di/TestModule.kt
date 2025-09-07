package com.zalamena.condominios.di

import com.zalamena.condominios.moradores.data.dao.MoradoresDao
import com.zalamena.condominios.moradores.data.mapper.MoradorMapper
import com.zalamena.condominios.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.moradores.database.MoradoresDaoTest
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import org.koin.dsl.module

val testModule = module {
    single<MoradoresDao> { MoradoresDaoTest() }
    single<MoradoresRepository> { MoradoresRepositoryImpl(get(), MoradorMapper()) } // Use test-friendly implementations
}