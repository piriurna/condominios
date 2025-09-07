package com.zalamena.moradores.usecase

import com.zalamena.condominios.moradores.domain.usecase.GetMoradoresUseCase
import com.zalamena.moradores.domain.repository.MoradoresRepository
import com.zalamena.moradores.domain.usecase.AddMoradorUseCase
import com.zalamena.moradores.domain.usecase.LoginMoradorUseCase

abstract class MoradorTest {
    protected lateinit var moradoresRepository: MoradoresRepository

    protected val addMoradorUseCase = AddMoradorUseCase(moradoresRepository)

    protected val loginMoradorUseCase = LoginMoradorUseCase(moradoresRepository)

    protected val getMoradoresUseCase = GetMoradoresUseCase(moradoresRepository)
}