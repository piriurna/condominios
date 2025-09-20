package com.zalamena.condominios.di

import com.zalamena.condominios.addapartamento.domain.usecases.AddApartamentoUseCase
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCase
import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCaseImpl
import com.zalamena.condominios.addmorador.ui.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.addmorador.ui.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.addpessoa.domain.usecase.AddPessoaUseCase
import com.zalamena.condominios.addpessoa.domain.usecase.AddPessoaUseCaseImpl
import com.zalamena.condominios.addpessoa.domain.validator.AddPessoaFormValidator
import com.zalamena.condominios.addpessoa.domain.validator.AddPessoaFormValidatorImpl
import com.zalamena.condominios.addpessoa.ui.AddPessoaViewModel
import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.repository.ApartamentoRepositoryImpl
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import com.zalamena.condominios.apartamentos.domain.usecase.GetApartamentoUseCase
import com.zalamena.condominios.apartamentos.domain.usecase.GetApartamentoUseCaseImpl
import com.zalamena.condominios.apartamentos.domain.usecase.GetApartamentosUseCase
import com.zalamena.condominios.database.AppDatabase
import com.zalamena.condominios.moradores.ui.add.AddMoradorViewModel
import com.zalamena.condominios.moradores.ui.list.MoradoresListViewModel
import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.repository.PessoaRepositoryImpl
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCaseImpl
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoasListUseCase
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.moradores.domain.repository.MoradoresRepository
import com.zalamena.moradores.domain.usecase.GetApartamentoWithMoradoresUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
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
    single<AddPessoaFormValidator> { AddPessoaFormValidatorImpl() }
}

val useCaseModule = module {
    factory<AddPessoaUseCase> { AddPessoaUseCaseImpl(get(), get()) }
    factory<GetPessoaUseCase> { GetPessoaUseCaseImpl(get()) }
    factory<GetApartamentoUseCase> { GetApartamentoUseCaseImpl(get()) }
    factory { GetPessoasListUseCase(get()) }
    factory { GetMoradoresUseCase(get()) }
    factory { GetMoradoresForApartamentoUseCase(get()) }
    factory { GetApartamentoWithMoradoresUseCase(get()) }
    factory { AddApartamentoUseCase(get()) }
    factory { GetApartamentosUseCase(get()) }
    factory<AddMoradorUseCase> { AddMoradorUseCaseImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::MoradoresListViewModel)
    viewModelOf(::AddMoradorViewModel)
    viewModelOf(::AddApartamentoViewModel)
    viewModelOf(::AddPessoaViewModel)
    viewModelOf(::AddMoradorOverviewViewModel)
    viewModelOf(::AddMoradorFlowViewModel)
}