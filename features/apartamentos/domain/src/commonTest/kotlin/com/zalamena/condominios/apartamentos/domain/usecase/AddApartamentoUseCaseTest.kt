package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.mapper.toApartamento
import com.zalamena.condominios.apartamentos.domain.models.AddApartamentoForm
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
    fun `GIVEN create id is success WHEN adding apartment THEN should be success`() = runTest {
        val addApartamentoForm = AddApartamentoForm.dummy
        val validId = "validId"

        everySuspending { apartamentosRepository.createApartamentoId(AddApartamentoForm.dummy.numero) } returns Result.success(validId)
        everySuspending { apartamentosRepository.addApartamento(addApartamentoForm.toApartamento(validId)) } returns Result.success(Unit)


        val addApartamentoResult = addApartamentoUseCase(addApartamentoForm)


        assertTrue(addApartamentoResult.isSuccess)
        assertTrue(addApartamentoResult.getOrNull() == Unit)
    }


    @Test
    fun `GIVEN error creating id WHEN adding apartment THEN should be not add apartment and fail`() = runTest {
        val addApartamentoForm = AddApartamentoForm.dummy
        val validId = "validId"
        everySuspending { apartamentosRepository.createApartamentoId(addApartamentoForm.numero) } returns Result.success(
            validId
        )
        everySuspending { apartamentosRepository.addApartamento(addApartamentoForm.toApartamento(validId)) } returns Result.failure(
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