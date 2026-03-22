package com.zalamena.condominios.di

import com.zalamena.condominios.condominio.data.apartamento.dao.ApartamentoDao
import com.zalamena.condominios.condominio.data.apartamento.repository.ApartamentoRepositoryImpl
import com.zalamena.condominios.condominio.data.condominio.dao.CondominioDao
import com.zalamena.condominios.condominio.data.condominio.repository.CondominioRepositoryImpl
import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.AddApartamentoUseCase
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCaseImpl
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentosUseCase
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.AddCondominioUseCase
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominioUseCase
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.AddMoradorUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.AddMoradorUseCaseImpl
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorDetailUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.RemoveMoradorUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.RemoveMoradorUseCaseImpl
import com.zalamena.condominios.condominio.domain.morador.usecase.UpdateMoradorTipoUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.UpdateMoradorTipoUseCaseImpl
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeViewModel
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.addmorador.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.overview.CondominioOverviewViewModel
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import com.zalamena.condominios.condominio.ui.porteiro.list.PorteiroListViewModel
import com.zalamena.condominios.condominio.domain.porteiro.models.PorteiroInfo
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroDeleter
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroProvider
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroReassigner
import com.zalamena.condominios.condominio.domain.porteiro.usecase.DeletePorteiroUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.GetPorteirosByCondominioUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.ReassignPorteiroUseCase
import com.zalamena.login.domain.models.UserRole
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
import com.zalamena.login.data.api.FakeLoginApi
import com.zalamena.login.data.api.LoginApi
import com.zalamena.login.data.repository.LoginRepositoryImpl
import com.zalamena.login.data.repository.SessionRepository
import com.zalamena.login.data.repository.SessionRepositoryImpl
import com.zalamena.login.data.repository.UserRepositoryImpl
import com.zalamena.login.domain.repository.LoginRepository
import com.zalamena.login.domain.repository.CondominioValidator
import com.zalamena.login.domain.repository.UserRepository
import com.zalamena.login.domain.usecase.CreateUserUseCase
import com.zalamena.login.domain.usecase.LoginUseCase
import com.zalamena.login.domain.usecase.LogoutUseCase
import com.zalamena.login.ui.LoginViewModel
import com.zalamena.login.ui.SplashViewModel
import com.zalamena.login.ui.createuser.CreateUserViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module


val daoModule = module {
    single<PessoaDao> { get<AppDatabase>().getPessoaDao() }
    single<ApartamentoDao> { get<AppDatabase>().getApartamentosDao() }
    single<CondominioDao> { get<AppDatabase>().getCondominioDao() }
    single<MoradoresDao> { get<AppDatabase>().getMoradoresDao() }
}

val repositoryModule = module {
    single<PessoaRepository> { PessoaRepositoryImpl(get()) }
    single<ApartamentosRepository> { ApartamentoRepositoryImpl(get()) }
    single<CondominioRepository> { CondominioRepositoryImpl(get()) }
    single<MoradoresRepository> { MoradoresRepositoryImpl(get()) }
    single<AddPessoaFormValidator> { AddPessoaFormValidatorImpl() }
    single<LoginApi> { FakeLoginApi(get()) }
    single<SessionRepository> { SessionRepositoryImpl() }
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl() }
    single<CondominioValidator> { CondominioValidator { id -> get<CondominioRepository>().getCondominio(id).isSuccess } }
    single<PorteiroProvider> {
        PorteiroProvider { condominioId ->
            get<UserRepository>().getUsersByCondominioId(condominioId).map { users ->
                users.map { user ->
                    PorteiroInfo(
                        id = user.id,
                        name = user.name,
                        cpf = user.cpf,
                        email = user.email,
                        condominioId = (user.role as UserRole.Porteiro).condominioId
                    )
                }
            }
        }
    }
    single<PorteiroDeleter> {
        PorteiroDeleter { porteiroId ->
            get<UserRepository>().deleteUser(porteiroId)
        }
    }
    single<PorteiroReassigner> {
        PorteiroReassigner { porteiroId, newCondominioId ->
            get<UserRepository>().updateUserRole(porteiroId, UserRole.Porteiro(newCondominioId))
        }
    }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory<AddPessoaUseCase> { AddPessoaUseCaseImpl(get(), get()) }
    factory<GetPessoaUseCase> { GetPessoaUseCaseImpl(get()) }
    factory<GetApartamentoUseCase> { GetApartamentoUseCaseImpl(get()) }
    factory { GetPessoasListUseCase(get()) }
    factory { GetCondominioUseCase(get()) }
    factory { GetCondominiosUseCase(get()) }
    factory { GetMoradoresUseCase(get<MoradoresRepository>()) }
    factory { AddApartamentoUseCase(get()) }
    factory { AddCondominioUseCase(get()) }
    factory { GetApartamentosUseCase(get()) }
    factory<AddMoradorUseCase> { AddMoradorUseCaseImpl(get()) }
    factory { GetMoradoresForApartamentoUseCase(get()) }
    factory { GetMoradorDetailUseCase(get()) }
    factory<RemoveMoradorUseCase> { RemoveMoradorUseCaseImpl(get()) }
    factory<UpdateMoradorTipoUseCase> { UpdateMoradorTipoUseCaseImpl(get()) }
    factory { CreateUserUseCase(get(), get()) }
    factory { GetPorteirosByCondominioUseCase(get()) }
    factory { DeletePorteiroUseCase(get()) }
    factory { ReassignPorteiroUseCase(get(), get()) }
}

val viewModelModule = module {
    viewModelOf(::MoradoresListViewModel)
    viewModelOf(::AddMoradorViewModel)
    viewModelOf(::AddApartamentoViewModel)
    viewModelOf(::AddPessoaViewModel)
    viewModelOf(::AddMoradorOverviewViewModel)
    viewModelOf(::AddMoradorFlowViewModel)
    viewModelOf(::CondominioOverviewViewModel)
    viewModelOf(::CondominioDashboardViewModel)
    viewModelOf(::AddCondominioViewModel)
    viewModelOf(::ApartamentoDetailViewModel)
    viewModelOf(::AdminHomeViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::CreateUserViewModel)
    viewModelOf(::PorteiroListViewModel)
    viewModelOf(::MoradorSearchViewModel)
    viewModelOf(::MoradorInfoViewModel)
}