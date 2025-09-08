package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.repository.MoradoresRepository
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks

abstract class MoradorTest: TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    protected val addMoradorUseCase by lazy { AddMoradorUseCase(moradoresRepository) }

    protected val getMoradoresUseCase by lazy {  GetMoradoresUseCase(moradoresRepository) }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}