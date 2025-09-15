package com.zalamena.condominios.addpessoa.domain.usecase

import com.zalamena.condominios.addpessoa.domain.mapper.toPessoa
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaException
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaForm
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaFormError
import com.zalamena.condominios.addpessoa.domain.validator.AddPessoaFormValidator
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.getValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddPessoaUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var pessoaRepository: PessoaRepository

    @Mock
    lateinit var addPessoaFormValidator: AddPessoaFormValidator

    private val addPessoaUseCase by lazy { AddPessoaUseCaseImpl(pessoaRepository, addPessoaFormValidator) }

    @Test
    fun `GIVEN user added is valid WHEN adding user THEN it should be successfully added`() = runTest {
        val addPessoaForm = AddPessoaForm.dummy
        val newPessoaId = "validId"

        onValidateFormReturns(emptyList())
        onCreateIdReturns(newPessoaId)
        onAddPessoaReturns(addPessoaForm.toPessoa(newPessoaId))

        val addResult = addPessoaUseCase.invoke(addPessoaForm)

        assertTrue(addResult.isSuccess)
    }


    @Test
    fun `GIVEN there is an error creating user id WHEN adding user THEN it should fail adding user`() = runTest {
        val addPessoaForm = AddPessoaForm.dummy
        val validId = "validId"
        val createdPessoa = addPessoaForm.toPessoa(validId)

        onValidateFormReturns(emptyList())
        onCreateIdFails(PessoaException.DuplicatePessoaException)
        onCreateIdReturns(validId)
        everySuspending { pessoaRepository.addPessoa(createdPessoa) } returns Result.success(createdPessoa)

        val addResult = addPessoaUseCase.invoke(addPessoaForm)

        assertTrue(addResult.isFailure)
        assertEquals(PessoaException.DuplicatePessoaException,addResult.exceptionOrNull())
        verifyWithSuspend {
            addPessoaFormValidator.validate(addPessoaForm)
            pessoaRepository.createPessoaId(
                addPessoaForm.cpf,
                addPessoaForm.nome,
                addPessoaForm.email,
                addPessoaForm.telefone
            )
        }
    }

    @Test
    fun `GIVEN pessoa form added is invalid WHEN adding user THEN it should fail adding user`() = runTest {
        val addPessoaForm = AddPessoaForm.dummy

        onValidateFormReturns(listOf(AddPessoaFormError.Nome))

        val addResult = addPessoaUseCase.invoke(addPessoaForm)

        assertTrue(addResult.isFailure)
        assertEquals(
            AddPessoaFormError.Nome,
            (addResult.exceptionOrNull() as AddPessoaException.FormValidationException).errors.first()
        )
    }

    private suspend fun onValidateFormReturns(errors: List<AddPessoaFormError>) {
        val addPessoaForm = AddPessoaForm.dummy
        everySuspending {
            addPessoaFormValidator.validate(addPessoaForm)
        } returns errors
    }

    private suspend fun onCreateIdReturns(id: String) {
        val addPessoaForm = AddPessoaForm.dummy
        everySuspending {
            pessoaRepository.createPessoaId(addPessoaForm.cpf, addPessoaForm.nome, addPessoaForm.email, addPessoaForm.telefone)
        } returns Result.success(id)
    }

    private suspend fun onAddPessoaReturns(pessoa: Pessoa) {
        everySuspending { pessoaRepository.addPessoa(pessoa) } returns Result.success(pessoa)
    }

    private suspend fun onCreateIdFails(e: Exception) {
        val addPessoaForm = AddPessoaForm.dummy
        everySuspending {
            pessoaRepository.createPessoaId(addPessoaForm.cpf, addPessoaForm.nome, addPessoaForm.email, addPessoaForm.telefone)
        } runs {
            Result.failure(e)
        }
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}