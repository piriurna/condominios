package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.moradores.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.moradores.usecase.GetMoradoresUseCase
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks

abstract class MoradorTest: TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    protected val getMoradoresUseCase by lazy { GetMoradoresUseCase(moradoresRepository) }

    protected val getMoradoresForApartamentoUseCase by lazy {
        GetMoradoresForApartamentoUseCase(
            moradoresRepository
        )
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}