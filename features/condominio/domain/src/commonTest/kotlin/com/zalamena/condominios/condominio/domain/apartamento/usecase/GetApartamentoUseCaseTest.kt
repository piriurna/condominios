package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetApartamentoUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val getApartamentoUseCase : GetApartamentoUseCase by lazy { GetApartamentoUseCaseImpl(apartamentosRepository) }


    @Test
    fun `GIVEN no apartamento is found WHEN getting apartamentos THEN should return failure`() = runTest {
        val apartamentoId = "id"
        everySuspending { apartamentosRepository.getApartamento(apartamentoId) } returns Result.failure(
            ApartamentoException.NoApartmentFoundException
        )

        val result = getApartamentoUseCase(apartamentoId)

        assertTrue(result.isFailure)
        assertEquals(ApartamentoException.NoApartmentFoundException, result.exceptionOrNull())
    }

    @Test
    fun `GIVEN apartamento is found WHEN getting apartamentos THEN should return list with apartamento`() = runTest {
        val apartamentoId = "id"
        val apartamentoResponse = Apartamento.dummy.copy(id = apartamentoId)
        everySuspending { apartamentosRepository.getApartamento(apartamentoId) } returns Result.success(
            apartamentoResponse
        )


        val result = getApartamentoUseCase(apartamentoId)


        assertTrue(result.isSuccess)
        assertEquals(apartamentoResponse, result.getOrThrow())

    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}