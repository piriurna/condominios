package com.zalamena.condominios.app.di

import com.zalamena.condominios.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val moradoresModule = module {
    singleOf(::MoradoresRepositoryImpl).bind(MoradoresRepository::class)
}