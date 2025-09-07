package com.zalamena.condominios.moradores.domain.usecase

import com.zalamena.condominios.moradores.data.mapper.MoradorMapper
import com.zalamena.condominios.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.condominios.moradores.database.MoradoresDaoTest
import com.zalamena.condominios.moradores.domain.models.Morador
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class LoginMoradoresUseCaseTest: MoradorTest() {
    @Test
    fun `GIVEN no user is logged in WHEN trying to login THEN should login the user`() {
        val newMorador = Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        val result = loginMoradorUseCase.invoke(newMorador).getOrNull()!!

        assertSame("Dummy Morador", result.nome)
        assertSame("101", result.apartamento)
        assertSame("12345678900", result.cpf)
    }


    @Test
    fun `GIVEN an user is logged in WHEN trying to login THEN should replace the user`() {
        val old = Morador(
            nome = "Dummy Old Morador",
            apartamento = "201",
            cpf = "12345678901"
        )

        loginMoradorUseCase.invoke(old)

        val newMorador = Morador(
            nome = "Dummy New Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        val result = loginMoradorUseCase.invoke(newMorador).getOrNull()!!

        assertSame("Dummy New Morador", result.nome)
        assertSame("101", result.apartamento)
        assertSame("12345678900", result.cpf)
    }

    @Test
    fun `GIVEN a user is trying to login WHEN there is no record of the morador THEN should add it before logging in`() {
        val newMorador = Morador(
            nome = "Dummy Morador",
            apartamento = "101",
            cpf = "12345678900"
        )

        var existingMorador = moradoresRepository.getMorador(newMorador)

        assertNull(existingMorador)

        loginMoradorUseCase.invoke(newMorador).getOrNull()!!

        existingMorador = moradoresRepository.getMorador(newMorador)

        assertNotNull(existingMorador)
    }
}