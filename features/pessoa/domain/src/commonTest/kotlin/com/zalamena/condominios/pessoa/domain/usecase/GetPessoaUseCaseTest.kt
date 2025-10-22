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

    private val getPessoaUseCase: GetPessoaUseCase by lazy { GetPessoaUseCaseImpl(pessoaRepository) }



    @Test
    fun `GIVEN no user with same id is added WHEN getting user THEN it should fail getting user`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getPessoa(newPessoa.id) } returns Result.failure(PessoaException.PessoaNotFoundException)

        val getResult = getPessoaUseCase.invoke(newPessoa.id)

        assertTrue(getResult.isFailure)
        assertEquals(PessoaException.PessoaNotFoundException, getResult.exceptionOrNull())
    }


    @Test
    fun `GIVEN user with same id is found WHEN getting user THEN it should successfully get user`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getPessoa(newPessoa.id) } returns Result.success(newPessoa)

        val addResult = getPessoaUseCase.invoke(newPessoa.id)

        assertTrue(addResult.isSuccess)
        assertEquals(newPessoa,addResult.getOrThrow())
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}