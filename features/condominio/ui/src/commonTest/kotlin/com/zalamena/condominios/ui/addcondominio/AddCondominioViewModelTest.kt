package com.zalamena.condominios.ui.addcondominio

import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.AddCondominioUseCase
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddCondominioViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val addCondominioUseCase by lazy { AddCondominioUseCase(condominioRepository) }
    private val viewModel by lazy { AddCondominioViewModel(addCondominioUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Initial state ---

    @Test
    fun `GIVEN initial state WHEN observing THEN isLoading is false`() = runTest {
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no error`() = runTest {
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no createdCondominioId`() = runTest {
        assertNull(viewModel.uiState.value.createdCondominioId)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN form is blank`() = runTest {
        val form = viewModel.uiState.value.form
        assertTrue(form.nome.isBlank())
        assertTrue(form.rua.isBlank())
        assertTrue(form.cep.isBlank())
    }

    // --- Form setters ---

    @Test
    fun `WHEN setting nome THEN form nome is updated`() = runTest {
        viewModel.setNome("Edifício Central")
        assertEquals("Edifício Central", viewModel.uiState.value.form.nome)
    }

    @Test
    fun `WHEN setting rua THEN form rua is updated`() = runTest {
        viewModel.setRua("Rua das Flores")
        assertEquals("Rua das Flores", viewModel.uiState.value.form.rua)
    }

    @Test
    fun `WHEN setting cep THEN form cep is updated`() = runTest {
        viewModel.setCep("12345-678")
        assertEquals("12345-678", viewModel.uiState.value.form.cep)
    }

    // --- addCondominio ---

    @Test
    fun `GIVEN valid form WHEN adding THEN sets createdCondominioId on success`() = runTest {
        fillValidForm()
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.success(Unit)

        viewModel.addCondominio()

        assertNotNull(viewModel.uiState.value.createdCondominioId)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `GIVEN blank form WHEN adding THEN sets error message`() = runTest {
        viewModel.addCondominio()

        assertEquals("Preencha todos os campos", viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.createdCondominioId)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN valid form WHEN repo fails THEN sets error message`() = runTest {
        fillValidForm()
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.failure(Exception("DB error"))

        viewModel.addCondominio()

        assertEquals("DB error", viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.createdCondominioId)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN error set WHEN changing form field THEN error is cleared`() = runTest {
        viewModel.addCondominio() // triggers blank form error
        assertEquals("Preencha todos os campos", viewModel.uiState.value.errorMessage)

        viewModel.setNome("Nome")

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // --- reset ---

    @Test
    fun `GIVEN form filled and condominio created WHEN reset THEN state is cleared`() = runTest {
        fillValidForm()
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.success(Unit)
        viewModel.addCondominio()

        viewModel.reset()

        val state = viewModel.uiState.value
        assertNull(state.createdCondominioId)
        assertNull(state.errorMessage)
        assertFalse(state.isLoading)
        assertTrue(state.form.nome.isBlank())
        assertTrue(state.form.rua.isBlank())
    }

    // --- Helpers ---

    private fun fillValidForm() {
        viewModel.setNome("Condomínio Teste")
        viewModel.setRua("Rua das Flores")
        viewModel.setNumero("123")
        viewModel.setCep("12345-678")
        viewModel.setCidade("São Paulo")
        viewModel.setEstado("SP")
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
