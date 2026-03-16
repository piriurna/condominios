package com.zalamena.condominios.pessoa.ui.addpessoa

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaException
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaFormError
import com.zalamena.condominios.pessoa.domain.addpessoa.usecase.AddPessoaUseCase
import com.zalamena.condominios.pessoa.ui.addpessoa.mapper.toDomain
import com.zalamena.condominios.pessoa.ui.addpessoa.models.AddPessoaFormUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AddPessoaViewModelTest: TestsWithMocks() {

    @Mock
    lateinit var addPessoaUseCase: AddPessoaUseCase

    private val viewModel by lazy { AddPessoaViewModel(addPessoaUseCase) }


    @BeforeTest
    fun setup() {
        val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN getting initial form THEN it should be all empty`() = runTest {
        val uiState = viewModel.uiState.value

        assertEquals("",uiState.addPessoaForm.telefone)
        assertEquals("",uiState.addPessoaForm.nome)
        assertEquals("",uiState.addPessoaForm.cpf)
        assertEquals("",uiState.addPessoaForm.telefone)
    }


    @Test
    fun `GIVEN telefone is empty WHEN updating form THEN it should be updated with given value`() = runTest {
        val telefone = "telefone"
        val testResults = mutableListOf<AddPessoaUiState>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        viewModel.updateForm(AddPessoaFormUiData(telefone = telefone))


        assertEquals("", testResults[0].addPessoaForm.telefone)
        assertEquals(telefone, testResults[1].addPessoaForm.telefone)
        job.cancel()
    }

    @Test
    fun `GIVEN form is valid WHEN submitting THEN it should call use case`() = runTest {
        val validForm = AddPessoaFormUiData()
        viewModel.updateForm(validForm)
        everySuspending { addPessoaUseCase.invoke(viewModel.uiState.value.addPessoaForm.toDomain()) } returns
                Result.success("")

        viewModel.addPessoa()

        verifyWithSuspend(exhaustive = false) {
            addPessoaUseCase(validForm.toDomain())
        }
    }

    @Test
    fun `GIVEN form is not valid WHEN submitting THEN it should show an error`() = runTest {
        val invalidForm = AddPessoaFormUiData()
        viewModel.updateForm(invalidForm)

        everySuspending { addPessoaUseCase.invoke(viewModel.uiState.value.addPessoaForm.toDomain()) } returns Result.failure(
            AddPessoaException.FormValidationException(listOf(AddPessoaFormError.Cpf))
        )

        viewModel.addPessoa()

        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(listOf(AddPessoaFormError.Cpf), uiState.formErrors)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}