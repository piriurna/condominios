package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMoradoresUseCaseTest: MoradorTest() {

    @Test
    fun `GIVEN no user is added WHEN getting all moradores THEN should return empty list`() {
        every { moradoresRepository.getAllMoradores() } returns emptyList()

        val moradores = getMoradoresUseCase()

        assertEquals(emptyList(), moradores)
    }


    @Test
    fun `GIVEN a user is added WHEN getting all moradores THEN should return a list with added morador`() {
        val addedMorador = Morador(
            nome = "Dummy Morado",
            apartamento = "101",
            cpf = "12345678900"
        )

        every { moradoresRepository.getAllMoradores() } returns listOf(addedMorador)

        val moradores = getMoradoresUseCase()

        assertEquals(listOf(addedMorador), moradores)
    }
}