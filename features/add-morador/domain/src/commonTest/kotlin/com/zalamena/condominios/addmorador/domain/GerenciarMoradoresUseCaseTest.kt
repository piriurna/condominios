package com.zalamena.condominios.addmorador.domain

import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCase
import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCaseImpl
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.moradores.domain.models.MoradorException
import com.zalamena.moradores.domain.repository.MoradoresRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GerenciarMoradoresUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val addMoradorUseCase: AddMoradorUseCase by lazy {
        AddMoradorUseCaseImpl(
            moradoresRepository
        )
    }

    @Test
    fun `GIVEN an pessoa is getting added to an existing apartamento WHEN there is no user with same id in the apartamento THEN should add it`() = runTest {
        everySuspending {
            moradoresRepository.getMorador(
                Pessoa.dummy.id,
                Apartamento.dummy.id
            )
        } returns Result.failure(MoradorException.MoradorNotFoundException)
        everySuspending {
            moradoresRepository.addMorador(
                Pessoa.dummy.id,
                Apartamento.dummy.id
            )
        } returns Result.success(Unit)

        val result = addMoradorUseCase.invoke(Pessoa.dummy.id, Apartamento.dummy.id)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN an pessoa is getting added to a non existent apartamento THEN should fail it`() = runTest {
        everySuspending {
            moradoresRepository.getMorador(
                Pessoa.dummy.id,
                Apartamento.dummy.id
            )
        } returns Result
            .failure(ApartamentoException.NoApartmentFoundException)

        val addResult = addMoradorUseCase.invoke(Pessoa.dummy.id, Apartamento.dummy.id)

        assertTrue(addResult.isFailure)
        assertEquals(ApartamentoException.NoApartmentFoundException, addResult.exceptionOrNull())
    }

    @Test
    fun `GIVEN an non existent pessoa is getting added to an existent apartamento THEN should fail it`() = runTest {
        everySuspending {
            moradoresRepository.getMorador(
                Pessoa.dummy.id,
                Apartamento.dummy.id
            )
        } returns Result
            .failure(PessoaException.PessoaNotFoundException)

        val addResult = addMoradorUseCase.invoke(Pessoa.dummy.id, Apartamento.dummy.id)

        assertTrue(addResult.isFailure)
        assertEquals(PessoaException.PessoaNotFoundException, addResult.exceptionOrNull())
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }


}