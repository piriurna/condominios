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

class GetApartamentoDetailsUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val getDetailsForApartamento by lazy { GetDetailsForApartamentoUseCase(apartamentosRepository) }

    @Test
    fun `GIVEN apartment is found WHEN getting details THEN should return apartment`() = runTest {
        everySuspending { apartamentosRepository.getApartamento("id") } returns Result.success(Apartamento.dummy)


        val apartamentoDetails = getDetailsForApartamento("id")


        assertTrue(apartamentoDetails.isSuccess)
        assertTrue(apartamentoDetails.getOrNull() == Apartamento.dummy)
    }


    @Test
    fun `GIVEN no apartment is found WHEN getting details THEN should fail with no apartment found error`() = runTest {
        everySuspending { apartamentosRepository.getApartamento("id") } returns
                Result.failure(ApartamentoException.NoApartmentFoundException)


        val apartamentoDetails = getDetailsForApartamento("id")


        assertTrue(apartamentoDetails.isFailure)
        assertTrue(apartamentoDetails.exceptionOrNull() is ApartamentoException.NoApartmentFoundException)
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}