package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.models.MoradorException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GerenciarMoradoresUseCaseTest: MoradorTest() {
    @BeforeTest
    fun setup() {
        setUpMocks()
    }

    @Test
    fun `GIVEN a user is getting added WHEN there is no user with same cpf THEN should add it`() {
        setUpMocks()

        val dummyMorador = Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )
        every { moradoresRepository.getMorador(dummyMorador.cpf) } returns Result.failure(MoradorException.MoradorNotFoundException)
        every { moradoresRepository.addMorador(dummyMorador) } returns Result.success(Unit)

        val result = addMoradorUseCase.invoke(dummyMorador)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN a user is getting added WHEN there is the same user added THEN should not add it`() {
        val newDuplicateMorador = Morador(
            nome ="Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        every { moradoresRepository.getMorador(newDuplicateMorador.cpf) } returns Result.success(newDuplicateMorador)
        every { moradoresRepository.addMorador(newDuplicateMorador) } returns Result.failure(
            MoradorException.DuplicateMoradorException)

        val addResult = addMoradorUseCase.invoke(newDuplicateMorador)

        assertTrue(addResult.isFailure)
    }
}