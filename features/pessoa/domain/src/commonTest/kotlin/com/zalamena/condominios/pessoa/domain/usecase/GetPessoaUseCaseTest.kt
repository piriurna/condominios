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

class GetPessoaUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var pessoaRepository: PessoaRepository

    private val getPessoaUseCase by lazy { GetPessoaUseCase(pessoaRepository) }



    @Test
    fun `GIVEN no user with same cpf is added WHEN getting user THEN it should fail getting user`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getPessoa(newPessoa.cpf) } returns Result.failure(PessoaException.PessoaNotFoundException)

        val getResult = getPessoaUseCase.invoke(newPessoa.cpf)

        assertTrue(getResult.isFailure)
        assertEquals(PessoaException.PessoaNotFoundException, getResult.exceptionOrNull())
    }


    @Test
    fun `GIVEN user with same cpf is found WHEN getting user THEN it should successfully get user`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getPessoa(newPessoa.cpf) } returns Result.success(newPessoa)

        val addResult = getPessoaUseCase.invoke(newPessoa.cpf)

        assertTrue(addResult.isSuccess)
        assertEquals(newPessoa,addResult.getOrThrow())
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}