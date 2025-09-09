package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMoradorUseCaseTest: MoradorTest() {

    val getMoradorUseCase by lazy {  GetMoradorUseCase(moradoresRepository) }

    @Test
    fun `GIVEN no user is added WHEN getting morador THEN should fail`() {
        every { moradoresRepository.getAllMoradores() } returns Result.success(emptyList())

        val moradorResult = getMoradorUseCase("cpf")

        assertTrue(moradorResult.isFailure)
    }


    @Test
    fun `GIVEN a user is added WHEN getting all moradores THEN should return a list with added morador`() {
        val addedMorador = Morador(
            nome = "Dummy Morado",
            apartamento = "101",
            cpf = "12345678900"
        )

        every { moradoresRepository.getMorador("12345678900") } returns Result.success(addedMorador)

        val moradorResult = getMoradorUseCase("12345678900")

        assertTrue(moradorResult.isSuccess)
        assertEquals(addedMorador, moradorResult.getOrThrow())
    }
}