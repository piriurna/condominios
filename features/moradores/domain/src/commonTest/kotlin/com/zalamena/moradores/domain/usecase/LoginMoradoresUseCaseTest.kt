package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class LoginMoradoresUseCaseTest: MoradorTest() {

    @BeforeTest
    fun setUp() {
        mocker.reset()
    }


    @Test
    fun `GIVEN the user trying to log in does not exist WHEN logging in THEN should add it to the app`() {
        val newMorador = Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        every { moradoresRepository.getCurrentLoggedMorador() } returns null
        every { moradoresRepository.getMorador(newMorador.cpf) } returns null
        every { moradoresRepository.loginMorador(newMorador) } runs {}
        every { moradoresRepository.addMorador(newMorador) } runs {}


        loginMoradorUseCase.invoke(newMorador)

        mocker.verify(exhaustive = false) {
            moradoresRepository.addMorador(newMorador)
        }
    }

    @Test
    fun `GIVEN no user is logged in WHEN trying to login THEN should login the user`() {
        val newMorador = Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        every { moradoresRepository.getCurrentLoggedMorador() } returns null
        every { moradoresRepository.getMorador(newMorador.cpf) } returns null
        every { moradoresRepository.loginMorador(newMorador) } runs {}
        every { moradoresRepository.addMorador(newMorador) } runs {}


        val result = loginMoradorUseCase.invoke(newMorador)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN a different user is logged in WHEN trying to login THEN should fail the login`() {
        val oldMorador = Morador(
            nome = "Old Morador",
            apartamento = "201",
            cpf = "12345678777"
        )

        val newMorador = Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        every { moradoresRepository.getCurrentLoggedMorador() } returns oldMorador
        every { moradoresRepository.getMorador(newMorador.cpf) } returns newMorador

        val result = loginMoradorUseCase.invoke(newMorador)

        assertTrue(result.isFailure)
    }
}