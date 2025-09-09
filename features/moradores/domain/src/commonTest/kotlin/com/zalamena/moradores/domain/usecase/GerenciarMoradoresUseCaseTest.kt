package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
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
        every { moradoresRepository.getMorador(dummyMorador.cpf) } returns null
        every { moradoresRepository.addMorador(dummyMorador) } runs {}

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

        every { moradoresRepository.getMorador(newDuplicateMorador.cpf) } returns newDuplicateMorador
        every { moradoresRepository.addMorador(newDuplicateMorador) } runs {}

        val addResult = addMoradorUseCase.invoke(newDuplicateMorador)

        assertFalse(addResult.isSuccess)
    }
}