package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.models.MoradorException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMoradorUseCaseTest: MoradorTest() {

    val getMoradorUseCase by lazy {  GetMoradorUseCase(moradoresRepository) }

    @Test
    fun `GIVEN no user is added WHEN getting morador THEN should fail`() = runTest {
        everySuspending { moradoresRepository.getMorador("cpf", "apartamentoId") } returns Result.failure(MoradorException.MoradorNotFoundException)

        val moradorResult = getMoradorUseCase("cpf", "apartamentoId")

        assertTrue(moradorResult.isFailure)
    }


    @Test
    fun `GIVEN a user is added WHEN getting all moradores THEN should return a list with added morador`() = runTest {
        val addedMorador = Morador.dummy

        everySuspending { moradoresRepository.getMorador("12345678900", "apartamentoId") } returns Result.success(addedMorador)

        val moradorResult = getMoradorUseCase("12345678900", "apartamentoId")

        assertTrue(moradorResult.isSuccess)
        assertEquals(addedMorador, moradorResult.getOrThrow())
    }
}