package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddPessoaUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var pessoaRepository: PessoaRepository

    private val addPessoaUseCase by lazy { AddPessoaUseCase(pessoaRepository) }



    @Test
    fun `GIVEN no user with same cpf is added WHEN adding user THEN it should be successfully added`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getPessoa(newPessoa.cpf) } returns Result.failure(PessoaException.PessoaNotFoundException)
        everySuspending { pessoaRepository.addPessoa(newPessoa) } returns Result.success(newPessoa)

        val addResult = addPessoaUseCase.invoke(newPessoa)

        assertTrue(addResult.isSuccess)
    }


    @Test
    fun `GIVEN user with same cpf is found WHEN adding user THEN it should fail adding user`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getPessoa(newPessoa.cpf) } returns Result.success(newPessoa)
        everySuspending { pessoaRepository.addPessoa(newPessoa) } returns Result.success(newPessoa)

        val addResult = addPessoaUseCase.invoke(newPessoa)

        assertTrue(addResult.isFailure)
        assertEquals(PessoaException.DuplicatePessoaException,addResult.exceptionOrNull())
        verifyWithSuspend {
            pessoaRepository.getPessoa(newPessoa.cpf)
        }
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}