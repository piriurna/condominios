package com.zalamena.condominios.ui.condominio

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.DashboardNavigationEvent
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
class CondominioDashboardViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getCondominiosUseCase by lazy { GetCondominiosUseCase(condominioRepository) }
    private val viewModel by lazy { CondominioDashboardViewModel(getCondominiosUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Initial state ---

    @Test
    fun `GIVEN initial state WHEN observing THEN isLoading is true`() = runTest(testScheduler) {
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN apartamentos list is empty`() = runTest(testScheduler) {
        assertEquals(emptyList(), viewModel.uiState.value.apartamentos)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN isError is false`() = runTest(testScheduler) {
        assertFalse(viewModel.uiState.value.isError)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no navigation event`() = runTest(testScheduler) {
        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- loadCondominios ---

    @Test
    fun `GIVEN condominioId set WHEN loading THEN populates apartamentos`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(Condominio.dummy.apartamentos.size, state.apartamentos.size)
        assertEquals(Condominio.dummy.nome, state.condominioNome)
    }

    @Test
    fun `GIVEN fetch fails WHEN loading THEN sets error state`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.failure(Exception("DB error"))

        viewModel.setCondominioId(Condominio.dummy.id)
        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
    }

    @Test
    fun `GIVEN condominio not found WHEN loading THEN sets error state`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())

        viewModel.setCondominioId("unknown-id")
        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
    }

    @Test
    fun `GIVEN no condominioId WHEN loading THEN does nothing`() = runTest(testScheduler) {
        viewModel.loadCondominios()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLoading)
    }

    // --- apartamento counts ---

    @Test
    fun `GIVEN condominio with apartamentos WHEN loading THEN totalApartamentos is correct`() = runTest(testScheduler) {
        val apt1 = Apartamento.dummy
        val apt2 = Apartamento.dummy.copy(id = "apt2", moradores = emptyList())
        val condominio = Condominio.dummy.copy(apartamentos = listOf(apt1, apt2))

        loadCondominio(condominio)

        assertEquals(2, viewModel.uiState.value.totalApartamentos)
    }

    @Test
    fun `GIVEN apartment with moradores WHEN loading THEN moradorCount is correct`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        val apt = viewModel.uiState.value.apartamentos.first()
        assertEquals(Apartamento.dummy.moradores.size, apt.moradorCount)
    }

    // --- Navigation events ---

    @Test
    fun `WHEN add apartamento clicked THEN navigation event is AddApartamento`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        viewModel.onAddApartamentoClick()

        val event = viewModel.uiState.value.navigationEvent
        assertIs<DashboardNavigationEvent.AddApartamento>(event)
        assertEquals(Condominio.dummy.id, event.condominioId)
    }

    @Test
    fun `WHEN apartamento clicked THEN navigation event is ApartamentoDetails with correct id`() = runTest(testScheduler) {
        viewModel.onApartamentoClick("apt-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<DashboardNavigationEvent.ApartamentoDetails>(event)
        assertEquals("apt-123", event.apartamentoId)
    }

    @Test
    fun `GIVEN nav event WHEN handled THEN navigation event is cleared`() = runTest(testScheduler) {
        viewModel.onApartamentoClick("apt-123")
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- Stale data / refresh ---

    @Test
    fun `GIVEN apartamentos loaded WHEN loadCondominios called again THEN apartamento list is refreshed`() = runTest(testScheduler) {
        val condominioId = Condominio.dummy.id
        val condominioEmpty = Condominio.dummy.copy(apartamentos = emptyList())
        val newApt = Apartamento.dummy.copy(id = "new-apt", moradores = emptyList())
        val condominioWithApt = condominioEmpty.copy(apartamentos = listOf(newApt))

        var response = Result.success(listOf(condominioEmpty))
        everySuspending { condominioRepository.getCondominios() } runs { response }

        viewModel.setCondominioId(condominioId)
        viewModel.loadCondominios()
        advanceUntilIdle()
        assertEquals(0, viewModel.uiState.value.apartamentos.size)

        response = Result.success(listOf(condominioWithApt))
        viewModel.loadCondominios()
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.apartamentos.size)
        assertEquals("new-apt", viewModel.uiState.value.apartamentos.first().id)
    }

    // --- Helpers ---

    private suspend fun loadCondominio(condominio: Condominio) {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(condominio))
        viewModel.setCondominioId(condominio.id)
        viewModel.loadCondominios()
        testScheduler.advanceUntilIdle()
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
