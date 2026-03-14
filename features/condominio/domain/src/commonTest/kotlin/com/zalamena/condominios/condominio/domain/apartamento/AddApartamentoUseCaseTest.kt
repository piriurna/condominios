package com.zalamena.condominios.condominio.domain.apartamento

import com.zalamena.condominios.condominio.domain.apartamento.mapper.toApartamento
import com.zalamena.condominios.condominio.domain.apartamento.model.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.AddApartamentoUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertTrue

class AddApartamentoUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val addApartamentoUseCase by lazy { AddApartamentoUseCase(apartamentosRepository) }

    @Test
    fun `GIVEN valid form WHEN adding apartment THEN should be success and return the created id`() = runTest {
        val addApartamentoForm = AddApartamentoForm.dummy
        val validId = "validId"

        everySuspending {
            apartamentosRepository.addApartamento(
                condominioId = addApartamentoForm.condominioId,
                apartamento = addApartamentoForm.toApartamento("")
            )
        } returns Result.success(validId)

        val result = addApartamentoUseCase(addApartamentoForm)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() == validId)
    }

    @Test
    fun `GIVEN repository fails WHEN adding apartment THEN should fail`() = runTest {
        val addApartamentoForm = AddApartamentoForm.dummy

        everySuspending {
            apartamentosRepository.addApartamento(
                condominioId = addApartamentoForm.condominioId,
                apartamento = addApartamentoForm.toApartamento("")
            )
        } returns Result.failure(ApartamentoException.DuplicatedApartmentException)

        val result = addApartamentoUseCase(addApartamentoForm)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApartamentoException.DuplicatedApartmentException)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
