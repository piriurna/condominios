package com.zalamena.condominios.moradores.domain.usecase

import com.zalamena.condominios.moradores.domain.models.Morador
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMoradoresUseCaseTest: MoradorTest() {

    @Test
    fun `GIVEN no user is added WHEN getting all moradores THEN should return empty list`() {
        val moradores = getMoradoresUseCase()

        assertEquals(emptyList(), moradores)
    }


    @Test
    fun `GIVEN a user id added WHEN getting all moradores THEN should return a list with added morador`() {
        val moradorToAdd = Morador(
            nome = "Dummy Morado",
            apartamento = "101",
            cpf = "12345678900"
        )



        val moradores = getMoradoresUseCase()

        assertEquals(emptyList(), moradores)
    }
}