package com.zalamena.condominios.ui.addmorador.searchpessoa

import com.zalamena.condominios.condominio.domain.morador.repository.PessoaProvider
import com.zalamena.condominios.condominio.domain.morador.usecase.GetAvailablePessoasForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.ui.addmorador.searchpessoa.SearchPessoaNavigationEvent
import com.zalamena.condominios.condominio.ui.addmorador.searchpessoa.SearchPessoaViewModel
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPessoaViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var pessoaProvider: PessoaProvider

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getMoradoresForApartamentoUseCase by lazy { GetMoradoresForApartamentoUseCase(moradoresRepository) }
    private val getAvailablePessoasUseCase by lazy { GetAvailablePessoasForApartamentoUseCase(pessoaProvider, getMoradoresForApartamentoUseCase) }
    private val viewModel by lazy { SearchPessoaViewModel(getAvailablePessoasUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    // --- Initial state ---

    @Test
    fun `GIVEN initial state WHEN observing THEN query is empty`() = runTest(testScheduler) {
        assertEquals("", viewModel.uiState.value.query)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN pessoas list is empty`() = runTest(testScheduler) {
        assertEquals(emptyList(), viewModel.uiState.value.pessoas)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN isLoading is false`() = runTest(testScheduler) {
        assertFalse(viewModel.uiState.value.isLoading)
    }

    // --- Load pessoas ---

    @Test
    fun `GIVEN pessoas exist WHEN loadPessoas THEN pessoas are loaded`() = runTest(testScheduler) {
        loadPessoas(listOf(makePessoa("Joao", "12345678900")))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.pessoas.size)
    }

    @Test
    fun `GIVEN fetch fails WHEN loadPessoas THEN sets error state`() = runTest(testScheduler) {
        everySuspending { pessoaProvider.getAllPessoas() } returns Result.failure(Exception("DB error"))

        viewModel.loadPessoas()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isError)
    }

    // --- Search by name ---

    @Test
    fun `GIVEN pessoas loaded WHEN query matches name THEN filters results`() = runTest(testScheduler) {
        loadPessoas(listOf(
            makePessoa("Joao Silva", "12345678900"),
            makePessoa("Maria Santos", "98765432100"),
            makePessoa("Joao Pedro", "11122233344")
        ))

        viewModel.setQuery("Joao")

        assertEquals(2, viewModel.uiState.value.pessoas.size)
    }

    @Test
    fun `GIVEN pessoas loaded WHEN query matches name case insensitive THEN filters results`() = runTest(testScheduler) {
        loadPessoas(listOf(
            makePessoa("Joao Silva", "12345678900"),
            makePessoa("Maria Santos", "98765432100")
        ))

        viewModel.setQuery("joao")

        assertEquals(1, viewModel.uiState.value.pessoas.size)
        assertEquals("Joao Silva", viewModel.uiState.value.pessoas.first().nome)
    }

    // --- Search by CPF ---

    @Test
    fun `GIVEN pessoas loaded WHEN query matches CPF digits THEN filters results`() = runTest(testScheduler) {
        loadPessoas(listOf(
            makePessoa("Joao Silva", "12345678900"),
            makePessoa("Maria Santos", "98765432100")
        ))

        viewModel.setQuery("123456")

        assertEquals(1, viewModel.uiState.value.pessoas.size)
        assertEquals("Joao Silva", viewModel.uiState.value.pessoas.first().nome)
    }

    // --- Empty results ---

    @Test
    fun `GIVEN pessoas loaded WHEN query matches nothing THEN returns empty list`() = runTest(testScheduler) {
        loadPessoas(listOf(
            makePessoa("Joao Silva", "12345678900"),
            makePessoa("Maria Santos", "98765432100")
        ))

        viewModel.setQuery("xyz")

        assertTrue(viewModel.uiState.value.pessoas.isEmpty())
    }

    // --- Clear query ---

    @Test
    fun `GIVEN filtered results WHEN query cleared THEN shows all pessoas`() = runTest(testScheduler) {
        loadPessoas(listOf(
            makePessoa("Joao Silva", "12345678900"),
            makePessoa("Maria Santos", "98765432100")
        ))

        viewModel.setQuery("Joao")
        assertEquals(1, viewModel.uiState.value.pessoas.size)

        viewModel.setQuery("")
        assertEquals(2, viewModel.uiState.value.pessoas.size)
    }

    // --- Navigation: select pessoa ---

    @Test
    fun `WHEN pessoa clicked THEN emits SelectPessoa event`() = runTest(testScheduler) {
        viewModel.onPessoaClick("pessoa-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<SearchPessoaNavigationEvent.SelectPessoa>(event)
        assertEquals("pessoa-123", event.pessoaId)
    }

    @Test
    fun `GIVEN nav event WHEN handled THEN navigation event is cleared`() = runTest(testScheduler) {
        viewModel.onPessoaClick("pessoa-123")
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- Navigation: create new pessoa ---

    @Test
    fun `WHEN create new pessoa clicked THEN emits CreateNewPessoa event`() = runTest(testScheduler) {
        viewModel.onCreateNewPessoaClick()

        val event = viewModel.uiState.value.navigationEvent
        assertIs<SearchPessoaNavigationEvent.CreateNewPessoa>(event)
    }

    // --- CPF masking ---

    @Test
    fun `GIVEN pessoa loaded WHEN observing THEN CPF is masked`() = runTest(testScheduler) {
        loadPessoas(listOf(makePessoa("Joao Silva", "12345678900")))

        val pessoa = viewModel.uiState.value.pessoas.first()
        assertEquals("***.***.***-00", pessoa.maskedCpf)
    }

    // --- Helpers ---

    private fun makePessoa(nome: String, cpf: String): Pessoa = Pessoa(
        id = "pessoa-$nome",
        cpf = cpf,
        nome = nome,
        email = "$nome@test.com",
        telefone = "11999999999"
    )

    private suspend fun loadPessoas(pessoas: List<Pessoa>) {
        everySuspending { pessoaProvider.getAllPessoas() } returns Result.success(pessoas)
        viewModel.loadPessoas()
        testScheduler.advanceUntilIdle()
    }
}
