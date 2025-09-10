package com.zalamena.moradores.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.models.IndividuoException
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
    fun `GIVEN an individuo is getting added to an existing apartamento WHEN there is no user with same cpf in the apartamento THEN should add it`() = runTest {
        everySuspending { moradoresRepository.getMorador(Individuo.dummy.cpf, Apartamento.dummy.id) } returns Result.failure(MoradorException.MoradorNotFoundException)
        everySuspending { moradoresRepository.addMorador(Individuo.dummy, Apartamento.dummy) } returns Result.success(Unit)

        val result = addMoradorUseCase.invoke(Individuo.dummy, Apartamento.dummy)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN an individuo is getting added to a non existent apartamento THEN should fail it`() = runTest {
        everySuspending { moradoresRepository.getMorador(Individuo.dummy.cpf, Apartamento.dummy.id) } returns Result
            .failure(ApartamentoException.NoApartmentFoundException)

        val addResult = addMoradorUseCase.invoke(Individuo.dummy, Apartamento.dummy)

        assertTrue(addResult.isFailure)
        assertEquals(ApartamentoException.NoApartmentFoundException, addResult.exceptionOrNull())
    }

    @Test
    fun `GIVEN an non existent individuo is getting added to an existent apartamento THEN should fail it`() = runTest {
        everySuspending { moradoresRepository.getMorador(Individuo.dummy.cpf, Apartamento.dummy.id) } returns Result
            .failure(IndividuoException.IndividuoNotFoundException)

        val addResult = addMoradorUseCase.invoke(Individuo.dummy, Apartamento.dummy)

        assertTrue(addResult.isFailure)
        assertEquals(IndividuoException.IndividuoNotFoundException, addResult.exceptionOrNull())
    }
}