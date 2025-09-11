package com.zalamena.condominios.di

import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.repository.ApartamentoRepositoryImpl
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import com.zalamena.condominios.database.AppDatabase
import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.repository.PessoaRepositoryImpl
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import com.zalamena.condominios.pessoa.domain.usecase.AddPessoaUseCase
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
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
    single<PessoaDao> { get<AppDatabase>().getPessoaDao() }
    single<ApartamentoDao> { get<AppDatabase>().getApartamentosDao() }
    single<MoradoresDao> { get<AppDatabase>().getMoradoresDao() }
}

// You can also create separate modules for better organization
val repositoryModule = module {
    single<PessoaRepository> { PessoaRepositoryImpl(get()) }
    single<ApartamentosRepository> { ApartamentoRepositoryImpl(get()) }
    single<MoradoresRepository> { MoradoresRepositoryImpl(get(), get(), MoradorMapper()) }
}

val useCaseModule = module {
    factory { AddPessoaUseCase(get()) }
    factory { GetPessoaUseCase(get()) }
    factory { AddMoradorUseCase(get()) }
    factory { GetMoradoresUseCase(get()) }
    factory { GetMoradoresForApartamentoUseCase(get()) }
    factory { GetApartamentoWithMoradoresUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MoradoresListViewModel(get()) }
}