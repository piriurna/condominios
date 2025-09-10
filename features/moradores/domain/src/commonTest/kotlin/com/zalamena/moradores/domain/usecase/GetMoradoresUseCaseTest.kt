package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMoradoresUseCaseTest: MoradorTest() {

    @Test
    fun `GIVEN no user is added WHEN getting all moradores THEN should return empty list`() = runTest {
        everySuspending { moradoresRepository.getAllMoradores() } returns Result.success(emptyList())

        val moradores = getMoradoresUseCase()

        assertEquals(emptyList(), moradores.getOrThrow())
    }


    @Test
    fun `GIVEN a user is added WHEN getting all moradores THEN should return a list with added morador`() = runTest {
        val addedMorador = Morador.dummy

        everySuspending { moradoresRepository.getAllMoradores() } returns Result.success(listOf(addedMorador))

        val moradores = getMoradoresUseCase()

        assertEquals(listOf(addedMorador), moradores.getOrThrow())
    }
}