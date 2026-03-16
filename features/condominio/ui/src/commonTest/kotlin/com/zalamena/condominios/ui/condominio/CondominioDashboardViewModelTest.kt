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
    fun `GIVEN initial state WHEN observing THEN isLoading is false`() = runTest(testScheduler) {
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN condominios list is empty`() = runTest(testScheduler) {
        assertEquals(emptyList(), viewModel.uiState.value.condominios)
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
    fun `GIVEN condominios available WHEN loading THEN populates condominios list`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(Condominio.dummy))

        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.condominios.size)
        assertEquals(Condominio.dummy.id, state.condominios.first().id)
        assertEquals(Condominio.dummy.nome, state.condominios.first().nome)
    }

    @Test
    fun `GIVEN fetch fails WHEN loading condominios THEN sets error state`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.failure(Exception("DB error"))

        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertEquals(emptyList(), state.condominios)
    }

    @Test
    fun `GIVEN no condominios WHEN loading THEN condominios list is empty`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())

        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(emptyList(), state.condominios)
    }

    // --- selectCondominio ---

    @Test
    fun `GIVEN condominio loaded WHEN selecting it THEN updates selectedCondominioId`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio(Condominio.dummy.id)

        assertEquals(Condominio.dummy.id, viewModel.uiState.value.selectedCondominioId)
    }

    @Test
    fun `GIVEN condominio loaded WHEN selecting it THEN populates apartamentos list`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio(Condominio.dummy.id)

        val apartamentos = viewModel.uiState.value.apartamentos
        assertEquals(Condominio.dummy.apartamentos.size, apartamentos.size)
        assertEquals(Condominio.dummy.apartamentos.first().id, apartamentos.first().id)
    }

    @Test
    fun `GIVEN condominio with apartamentos WHEN selecting THEN totalApartamentos is correct`() = runTest(testScheduler) {
        val apt1 = Apartamento.dummy
        val apt2 = Apartamento.dummy.copy(id = "apt2", moradores = emptyList())
        val condominio = Condominio.dummy.copy(apartamentos = listOf(apt1, apt2))

        loadCondominio(condominio)
        viewModel.selectCondominio(condominio.id)

        assertEquals(2, viewModel.uiState.value.totalApartamentos)
    }

    @Test
    fun `GIVEN apartment with moradores WHEN selecting condominio THEN moradorCount is correct`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio(Condominio.dummy.id)

        val apt = viewModel.uiState.value.apartamentos.first()
        assertEquals(Apartamento.dummy.moradores.size, apt.moradorCount)
    }

    @Test
    fun `GIVEN apartment with no moradores WHEN selecting condominio THEN moradorCount is zero`() = runTest(testScheduler) {
        val empty = Apartamento.dummy.copy(moradores = emptyList())
        loadCondominio(Condominio.dummy.copy(apartamentos = listOf(empty)))

        viewModel.selectCondominio(Condominio.dummy.id)

        assertEquals(0, viewModel.uiState.value.apartamentos.first().moradorCount)
    }

    @Test
    fun `GIVEN unknown condominioId WHEN selecting THEN state does not change`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio("unknown-id")

        assertNull(viewModel.uiState.value.selectedCondominioId)
        assertEquals(emptyList(), viewModel.uiState.value.apartamentos)
    }

    // --- Navigation events ---

    @Test
    fun `WHEN add apartamento clicked THEN navigation event is AddApartamento`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)
        viewModel.selectCondominio(Condominio.dummy.id)

        viewModel.onAddApartamentoClick()

        assertIs<DashboardNavigationEvent.AddApartamento>(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `WHEN apartamento clicked THEN navigation event is ApartamentoDetails with correct id`() = runTest(testScheduler) {
        viewModel.onApartamentoClick("apt-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<DashboardNavigationEvent.ApartamentoDetails>(event)
        assertEquals("apt-123", event.apartamentoId)
    }

    @Test
    fun `GIVEN AddApartamento nav event WHEN handled THEN navigation event is cleared`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)
        viewModel.selectCondominio(Condominio.dummy.id)
        viewModel.onAddApartamentoClick()
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN ApartamentoDetails nav event WHEN handled THEN navigation event is cleared`() = runTest(testScheduler) {
        viewModel.onApartamentoClick("apt-123")
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `WHEN add apartamento clicked with condominio selected THEN navigation event carries the condominioId`() = runTest(testScheduler) {
        loadCondominio(Condominio.dummy)
        viewModel.selectCondominio(Condominio.dummy.id)

        viewModel.onAddApartamentoClick()

        val event = viewModel.uiState.value.navigationEvent
        assertIs<DashboardNavigationEvent.AddApartamento>(event)
        assertEquals(Condominio.dummy.id, event.condominioId)
    }

    // --- Stale data / refresh ---

    @Test
    fun `GIVEN condominio selected WHEN loadCondominios called after apartamento added THEN apartamento list is refreshed`() = runTest(testScheduler) {
        val condominioId = Condominio.dummy.id
        val condominioEmpty = Condominio.dummy.copy(apartamentos = emptyList())
        val newApt = Apartamento.dummy.copy(id = "new-apt", moradores = emptyList())
        val condominioWithApt = condominioEmpty.copy(apartamentos = listOf(newApt))

        var response = Result.success(listOf(condominioEmpty))
        everySuspending { condominioRepository.getCondominios() } runs { response }

        viewModel.loadCondominios()
        advanceUntilIdle()
        viewModel.selectCondominio(condominioId)
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
        viewModel.loadCondominios()
        testScheduler.advanceUntilIdle()
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
