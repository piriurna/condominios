package com.zalamena.condominios.di

import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.repository.ApartamentoRepositoryImpl
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import com.zalamena.condominios.database.AppDatabase
import com.zalamena.condominios.individuo.data.dao.IndividuoDao
import com.zalamena.condominios.individuo.data.repository.IndividuoRepositoryImpl
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository
import com.zalamena.condominios.individuo.domain.usecase.AddIndividuoUseCase
import com.zalamena.condominios.individuo.domain.usecase.GetIndividuoUseCase
import com.zalamena.condominios.moradores.ui.list.MoradoresListViewModel
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.moradores.domain.repository.MoradoresRepository
import com.zalamena.moradores.domain.usecase.AddMoradorUseCase
import com.zalamena.moradores.domain.usecase.GetApartamentoWithMoradoresUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module


val daoModule = module {
    single<IndividuoDao> { get<AppDatabase>().getIndividuoDao() }
    single<ApartamentoDao> { get<AppDatabase>().getApartamentosDao() }
    single<MoradoresDao> { get<AppDatabase>().getMoradoresDao() }
}

// You can also create separate modules for better organization
val repositoryModule = module {
    single<IndividuoRepository> { IndividuoRepositoryImpl(get()) }
    single<ApartamentosRepository> { ApartamentoRepositoryImpl(get()) }
    single<MoradoresRepository> { MoradoresRepositoryImpl(get(), get(), MoradorMapper()) }
}

val useCaseModule = module {
    factory { AddIndividuoUseCase(get()) }
    factory { GetIndividuoUseCase(get()) }
    factory { AddMoradorUseCase(get()) }
    factory { GetMoradoresUseCase(get()) }
    factory { GetMoradoresForApartamentoUseCase(get()) }
    factory { GetApartamentoWithMoradoresUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MoradoresListViewModel(get()) }
}