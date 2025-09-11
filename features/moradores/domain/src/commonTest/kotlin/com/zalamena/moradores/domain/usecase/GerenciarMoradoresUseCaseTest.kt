package com.zalamena.moradores.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.moradores.domain.models.MoradorException
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GerenciarMoradoresUseCaseTest: MoradorTest() {
    @BeforeTest
    fun setup() {
        setUpMocks()
    }

    @Test
    fun `GIVEN an pessoa is getting added to an existing apartamento WHEN there is no user with same cpf in the apartamento THEN should add it`() = runTest {
        everySuspending { moradoresRepository.getMorador(Pessoa.dummy.cpf, Apartamento.dummy.id) } returns Result.failure(MoradorException.MoradorNotFoundException)
        everySuspending { moradoresRepository.addMorador(Pessoa.dummy, Apartamento.dummy) } returns Result.success(Unit)

        val result = addMoradorUseCase.invoke(Pessoa.dummy, Apartamento.dummy)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN an pessoa is getting added to a non existent apartamento THEN should fail it`() = runTest {
        everySuspending { moradoresRepository.getMorador(Pessoa.dummy.cpf, Apartamento.dummy.id) } returns Result
            .failure(ApartamentoException.NoApartmentFoundException)

        val addResult = addMoradorUseCase.invoke(Pessoa.dummy, Apartamento.dummy)

        assertTrue(addResult.isFailure)
        assertEquals(ApartamentoException.NoApartmentFoundException, addResult.exceptionOrNull())
    }

    @Test
    fun `GIVEN an non existent pessoa is getting added to an existent apartamento THEN should fail it`() = runTest {
        everySuspending { moradoresRepository.getMorador(Pessoa.dummy.cpf, Apartamento.dummy.id) } returns Result
            .failure(PessoaException.PessoaNotFoundException)

        val addResult = addMoradorUseCase.invoke(Pessoa.dummy, Apartamento.dummy)

        assertTrue(addResult.isFailure)
        assertEquals(PessoaException.PessoaNotFoundException, addResult.exceptionOrNull())
    }
}