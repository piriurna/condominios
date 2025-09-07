package com.zalamena.condominios.moradores.domain.usecase

import com.zalamena.condominios.moradores.data.mapper.MoradorMapper
import com.zalamena.condominios.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.moradores.database.MoradoresDaoTest
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import org.koin.test.KoinTest

abstract class MoradorTest: KoinTest {
    protected val moradoresRepository: MoradoresRepository = MoradoresRepositoryImpl(MoradoresDaoTest(), MoradorMapper())


    protected val addMoradorUseCase = AddMoradorUseCase(moradoresRepository)

    protected val loginMoradorUseCase = LoginMoradorUseCase(moradoresRepository)

    protected val getMoradoresUseCase = GetMoradoresUseCase(moradoresRepository)
}