package com.zalamena.condominios.moradores.domain.usecase

import com.zalamena.condominios.moradores.domain.models.Morador
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GerenciarMoradoresUseCaseTest: MoradorTest() {
    @Test
    fun `GIVEN a user is getting added WHEN there is no user with same apartment and name THEN should add it`() {
        val result = addMoradorUseCase.invoke(Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        ))


        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN a user is getting added WHEN there is a user with same apartment and name THEN should not add it`() {

        val firstResult = addMoradorUseCase.invoke(Morador(
            nome ="Dummy First Morador",
            apartamento = "201",
            cpf = "12345678900"
        ))

        assertTrue(firstResult.isSuccess)

        val secondResult = addMoradorUseCase.invoke(Morador(
            nome ="Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        ))

        assertFalse(secondResult.isSuccess)
    }
}