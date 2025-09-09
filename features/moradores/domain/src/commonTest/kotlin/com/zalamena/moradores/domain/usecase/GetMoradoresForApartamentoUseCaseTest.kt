package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMoradoresForApartamentoUseCaseTest: MoradorTest() {

    @Test
    fun `GIVEN no user is added WHEN getting all moradores THEN should return empty list`() {
        every { moradoresRepository.getAllMoradoresForApartamento("id") } returns Result.success(emptyList())

        val moradores = getMoradoresForApartamentoUseCase("id")

        assertEquals(emptyList(), moradores.getOrThrow())
    }


    @Test
    fun `GIVEN a user is added WHEN getting all moradores THEN should return a list with added morador`() {
        every { moradoresRepository.getAllMoradoresForApartamento("id") } returns Result.success(listOf(Morador.dummy))

        val moradores = getMoradoresForApartamentoUseCase("id")

        assertEquals(listOf(Morador.dummy), moradores.getOrThrow())
    }
}