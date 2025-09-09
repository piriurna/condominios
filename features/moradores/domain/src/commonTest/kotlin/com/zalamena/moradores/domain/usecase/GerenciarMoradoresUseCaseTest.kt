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
        every { moradoresRepository.getMorador(Morador.dummy.cpf) } returns Result.failure(MoradorException.MoradorNotFoundException)
        every { moradoresRepository.addMorador(Morador.dummy) } returns Result.success(Unit)

        val result = addMoradorUseCase.invoke(Morador.dummy)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN a user is getting added WHEN there is the same user added THEN should not add it`() {
        val newDuplicateMorador = Morador.dummy

        every { moradoresRepository.getMorador(newDuplicateMorador.cpf) } returns Result.success(newDuplicateMorador)
        every { moradoresRepository.addMorador(newDuplicateMorador) } returns Result.failure(
            MoradorException.DuplicateMoradorException)

        val addResult = addMoradorUseCase.invoke(newDuplicateMorador)

        assertTrue(addResult.isFailure)
    }
}