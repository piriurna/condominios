package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.usecase.GetDetailsForApartamentoUseCase
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertTrue

class AddApartamentoUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val addApartamentoUseCase by lazy { AddApartamentoUseCase(apartamentosRepository) }

    @Test
    fun `GIVEN no apartment is found WHEN adding apartment THEN should be success`() = runTest {
        everySuspending { apartamentosRepository.addApartamento(Apartamento.dummy) } returns Result.success(Unit)
        everySuspending { apartamentosRepository.getApartamento(Apartamento.dummy.id) } returns Result.failure(
            ApartamentoException.NoApartmentFoundException
        )


        val addApartamentoResult = addApartamentoUseCase(Apartamento.dummy)


        assertTrue(addApartamentoResult.isSuccess)
        assertTrue(addApartamentoResult.getOrNull() == Unit)
    }


    @Test
    fun `GIVEN apartment is found WHEN adding apartment THEN should be not add apartment and fail`() = runTest {
        everySuspending { apartamentosRepository.getApartamento(Apartamento.dummy.id) } returns Result.success(
            Apartamento.dummy
        )


        val addApartamentoResult = addApartamentoUseCase(Apartamento.dummy)


        assertTrue(addApartamentoResult.isFailure)
        assertTrue(addApartamentoResult.exceptionOrNull() is ApartamentoException.DuplicatedApartmentException)
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}