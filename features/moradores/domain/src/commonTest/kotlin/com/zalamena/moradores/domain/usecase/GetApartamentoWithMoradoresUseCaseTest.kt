package com.zalamena.moradores.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.moradores.domain.models.ApartamentoWithMoradores
import com.zalamena.moradores.domain.repository.MoradoresRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetApartamentoWithMoradoresUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository


    val getApartamentoWithMoradoresUseCase by lazy {  GetApartamentoWithMoradoresUseCase(moradoresRepository) }

    @Test
    fun `GIVEN apartamento has 1 individuo WHEN getting apartamento with moradores THEN should return apartamento information with the given morador`() = runTest {
        everySuspending { moradoresRepository.getApartamentoWithMoradores("apartamentoId") } returns Result
            .success(ApartamentoWithMoradores.dummy)

        val result = getApartamentoWithMoradoresUseCase("apartamentoId")


        assertTrue(result.isSuccess)
        assertEquals(ApartamentoWithMoradores.dummy, result.getOrThrow())
    }


    @Test
    fun `GIVEN apartamento has no one WHEN getting apartamento with moradores THEN should return apartamento information with no moradores`() = runTest {
        everySuspending { moradoresRepository.getApartamentoWithMoradores("apartamentoId") } returns Result
            .success(ApartamentoWithMoradores.dummy.copy(moradores = emptyList()))

        val result = getApartamentoWithMoradoresUseCase("apartamentoId")


        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow().moradores)
    }

    @Test
    fun `GIVEN apartamento is not found WHEN getting apartamento with moradores THEN should return fail`() = runTest {
        everySuspending { moradoresRepository.getApartamentoWithMoradores("apartamentoId") } returns Result
            .failure(ApartamentoException.NoApartmentFoundException)

        val result = getApartamentoWithMoradoresUseCase("apartamentoId")


        assertTrue(result.isFailure)
        assertEquals(ApartamentoException.NoApartmentFoundException, result.exceptionOrNull())
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}