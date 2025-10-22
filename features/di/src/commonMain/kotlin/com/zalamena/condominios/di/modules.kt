package com.zalamena.condominios.di

import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCase
import com.zalamena.condominios.condominio.data.apartamento.dao.ApartamentoDao
import com.zalamena.condominios.condominio.data.apartamento.repository.ApartamentoRepositoryImpl
import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.mapper.MoradorMapper
import com.zalamena.condominios.condominio.data.moradores.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.condominio.domain.addapartamento.usecases.AddApartamentoUseCase
import com.zalamena.condominios.condominio.domain.addmorador.usecase.AddMoradorUseCaseImpl
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCaseImpl
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentosUseCase
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.moradores.usecase.GetApartamentoWithMoradoresUseCase
import com.zalamena.condominios.condominio.domain.moradores.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.moradores.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addmorador.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.condominio.ui.moradores.add.AddMoradorViewModel
import com.zalamena.condominios.condominio.ui.moradores.list.MoradoresListViewModel
import com.zalamena.condominios.database.AppDatabase
import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.repository.PessoaRepositoryImpl
import com.zalamena.condominios.pessoa.domain.addpessoa.usecase.AddPessoaUseCase
import com.zalamena.condominios.pessoa.domain.addpessoa.usecase.AddPessoaUseCaseImpl
import com.zalamena.condominios.pessoa.domain.addpessoa.validator.AddPessoaFormValidator
import com.zalamena.condominios.pessoa.domain.addpessoa.validator.AddPessoaFormValidatorImpl
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCaseImpl
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoasListUseCase
import com.zalamena.condominios.pessoa.ui.addpessoa.AddPessoaViewModel
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