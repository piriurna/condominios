package com.zalamena.condominios.ui.moradores

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchNavigationEvent
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
class MoradorSearchViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getMoradoresUseCase by lazy { GetMoradoresUseCase(moradoresRepository) }
    private val viewModel by lazy { MoradorSearchViewModel(getMoradoresUseCase) }

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
    fun `GIVEN initial state WHEN observing THEN moradores list is empty`() = runTest(testScheduler) {
        assertEquals(emptyList(), viewModel.uiState.value.moradores)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN isLoading is false`() = runTest(testScheduler) {
        assertFalse(viewModel.uiState.value.isLoading)
    }

    // --- Load moradores ---

    @Test
    fun `GIVEN condominioId set WHEN loadMoradores THEN moradores are loaded`() = runTest(testScheduler) {
        loadMoradores(listOf(makeMorador("João", "101")))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.moradores.size)
    }

    @Test
    fun `GIVEN fetch fails WHEN loadMoradores THEN sets error state`() = runTest(testScheduler) {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condo-1") } returns Result.failure(Exception("DB error"))

        viewModel.setCondominioId("condo-1")
        viewModel.loadMoradores()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isError)
    }

    @Test
    fun `GIVEN blank condominioId WHEN loadMoradores THEN does nothing`() = runTest(testScheduler) {
        viewModel.loadMoradores()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.moradores.isEmpty())
    }

    // --- Search by name ---

    @Test
    fun `GIVEN moradores loaded WHEN query matches name THEN filters results`() = runTest(testScheduler) {
        loadMoradores(listOf(
            makeMorador("João Silva", "101"),
            makeMorador("Maria Santos", "102"),
            makeMorador("João Pedro", "103")
        ))

        viewModel.setQuery("João")

        assertEquals(2, viewModel.uiState.value.moradores.size)
    }

    @Test
    fun `GIVEN moradores loaded WHEN query matches name case insensitive THEN filters results`() = runTest(testScheduler) {
        loadMoradores(listOf(
            makeMorador("João Silva", "101"),
            makeMorador("Maria Santos", "102")
        ))

        viewModel.setQuery("joão")

        assertEquals(1, viewModel.uiState.value.moradores.size)
        assertEquals("João Silva", viewModel.uiState.value.moradores.first().nome)
    }

    // --- Search by apartment number ---

    @Test
    fun `GIVEN moradores loaded WHEN query matches apartment number THEN filters results`() = runTest(testScheduler) {
        loadMoradores(listOf(
            makeMorador("João Silva", "101"),
            makeMorador("Maria Santos", "102"),
            makeMorador("Pedro Costa", "101")
        ))

        viewModel.setQuery("101")

        assertEquals(2, viewModel.uiState.value.moradores.size)
    }

    @Test
    fun `GIVEN moradores loaded WHEN query matches partial apartment number THEN filters results`() = runTest(testScheduler) {
        loadMoradores(listOf(
            makeMorador("João Silva", "101"),
            makeMorador("Maria Santos", "201")
        ))

        viewModel.setQuery("01")

        assertEquals(2, viewModel.uiState.value.moradores.size)
    }

    // --- Empty results ---

    @Test
    fun `GIVEN moradores loaded WHEN query matches nothing THEN returns empty list`() = runTest(testScheduler) {
        loadMoradores(listOf(
            makeMorador("João Silva", "101"),
            makeMorador("Maria Santos", "102")
        ))

        viewModel.setQuery("xyz")

        assertTrue(viewModel.uiState.value.moradores.isEmpty())
    }

    // --- Clear query ---

    @Test
    fun `GIVEN filtered results WHEN query cleared THEN shows all moradores`() = runTest(testScheduler) {
        loadMoradores(listOf(
            makeMorador("João Silva", "101"),
            makeMorador("Maria Santos", "102")
        ))

        viewModel.setQuery("João")
        assertEquals(1, viewModel.uiState.value.moradores.size)

        viewModel.setQuery("")
        assertEquals(2, viewModel.uiState.value.moradores.size)
    }

    // --- Navigation ---

    @Test
    fun `WHEN morador clicked THEN emits MoradorDetail event`() = runTest(testScheduler) {
        viewModel.onMoradorClick("pessoa-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<MoradorSearchNavigationEvent.MoradorDetail>(event)
        assertEquals("pessoa-123", event.pessoaId)
    }

    @Test
    fun `GIVEN nav event WHEN handled THEN navigation event is cleared`() = runTest(testScheduler) {
        viewModel.onMoradorClick("pessoa-123")
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- Helpers ---

    private fun makeMorador(nome: String, aptNumero: String): Morador = Morador(
        pessoa = Pessoa(
            id = "pessoa-$nome",
            cpf = "12345678900",
            nome = nome,
            email = "$nome@test.com",
            telefone = "11999999999"
        ),
        apartamento = Apartamento(
            id = "apt-$aptNumero",
            numero = aptNumero,
            andar = "1",
            moradores = emptyList()
        ),
        tipo = MoradorTipo.RESIDENTE
    )

    private suspend fun loadMoradores(moradores: List<Morador>) {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condo-1") } returns Result.success(moradores)
        viewModel.setCondominioId("condo-1")
        viewModel.loadMoradores()
        testScheduler.advanceUntilIdle()
    }
}
