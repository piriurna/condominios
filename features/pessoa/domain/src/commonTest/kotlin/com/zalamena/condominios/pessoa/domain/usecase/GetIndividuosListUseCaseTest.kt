package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPessoasListUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var pessoaRepository: PessoaRepository

    private val getPessoasListUseCase by lazy { GetPessoasListUseCase(pessoaRepository) }



    @Test
    fun `GIVEN no user is added WHEN getting user list THEN it should return empty list`() = runTest {
        everySuspending { pessoaRepository.getAllIndividos() } returns Result.success(emptyList())

        val getResult = getPessoasListUseCase.invoke()

        assertTrue(getResult.isSuccess)
        assertEquals(emptyList(), getResult.getOrThrow())
    }


    @Test
    fun `GIVEN user is added WHEN getting user list THEN it should successfully get user`() = runTest {
        val newPessoa = Pessoa.dummy

        everySuspending { pessoaRepository.getAllIndividos() } returns Result.success(listOf(newPessoa))

        val addResult = getPessoasListUseCase.invoke()

        assertTrue(addResult.isSuccess)
        assertEquals(listOf(newPessoa),addResult.getOrThrow())
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}