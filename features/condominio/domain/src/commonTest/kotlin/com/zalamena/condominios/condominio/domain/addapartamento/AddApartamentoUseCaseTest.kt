package com.zalamena.condominios.condominio.domain.addapartamento

import com.zalamena.condominios.condominio.domain.addapartamento.mapper.toApartamento
import com.zalamena.condominios.condominio.domain.addapartamento.models.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.addapartamento.usecases.AddApartamentoUseCase
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
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
    fun `GIVEN create id is success WHEN adding apartment THEN should be success and return the created id`() = runTest {
        val addApartamentoForm = AddApartamentoForm.Companion.dummy
        val validId = "validId"

        everySuspending { apartamentosRepository.createApartamentoId(AddApartamentoForm.Companion.dummy.numero) } returns Result.success(
            validId
        )
        everySuspending {
            apartamentosRepository.addApartamento(
                addApartamentoForm.toApartamento(
                    validId
                )
            )
        } returns Result.success(Unit)


        val addApartamentoResult = addApartamentoUseCase(addApartamentoForm)


        assertTrue(addApartamentoResult.isSuccess)
        assertTrue(addApartamentoResult.getOrNull() == validId)
    }


    @Test
    fun `GIVEN error creating id WHEN adding apartment THEN should be not add apartment and fail`() =
        runTest {
            val addApartamentoForm = AddApartamentoForm.Companion.dummy
            val validId = "validId"
            everySuspending { apartamentosRepository.createApartamentoId(addApartamentoForm.numero) } returns Result.success(
                validId
            )
            everySuspending {
                apartamentosRepository.addApartamento(
                    addApartamentoForm.toApartamento(
                        validId
                    )
                )
            } returns Result.failure(
                ApartamentoException.DuplicatedApartmentException
            )


            val addApartamentoResult = addApartamentoUseCase(addApartamentoForm)


            assertTrue(addApartamentoResult.isFailure)
            assertTrue(addApartamentoResult.exceptionOrNull() is ApartamentoException.DuplicatedApartmentException)
        }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}