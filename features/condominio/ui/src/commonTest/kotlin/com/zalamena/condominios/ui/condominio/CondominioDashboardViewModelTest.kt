package com.zalamena.condominios.ui.condominio

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.DashboardNavigationEvent
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
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CondominioDashboardViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val getCondominiosUseCase by lazy { GetCondominiosUseCase(condominioRepository) }
    private val viewModel by lazy { CondominioDashboardViewModel(getCondominiosUseCase) }

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
    fun `GIVEN initial state WHEN observing THEN condominios list is empty`() = runTest {
        assertEquals(emptyList(), viewModel.uiState.value.condominios)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN isError is false`() = runTest {
        assertFalse(viewModel.uiState.value.isError)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no navigation event`() = runTest {
        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- loadCondominios ---

    @Test
    fun `GIVEN condominios available WHEN loading THEN populates condominios list`() = runTest {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(Condominio.dummy))

        viewModel.loadCondominios()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.condominios.size)
        assertEquals(Condominio.dummy.id, state.condominios.first().id)
        assertEquals(Condominio.dummy.nome, state.condominios.first().nome)
    }

    @Test
    fun `GIVEN fetch fails WHEN loading condominios THEN sets error state`() = runTest {
        everySuspending { condominioRepository.getCondominios() } returns Result.failure(Exception("DB error"))

        viewModel.loadCondominios()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertEquals(emptyList(), state.condominios)
    }

    @Test
    fun `GIVEN no condominios WHEN loading THEN condominios list is empty`() = runTest {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())

        viewModel.loadCondominios()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(emptyList(), state.condominios)
    }

    // --- selectCondominio ---

    @Test
    fun `GIVEN condominio loaded WHEN selecting it THEN updates selectedCondominioId`() = runTest {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio(Condominio.dummy.id)

        assertEquals(Condominio.dummy.id, viewModel.uiState.value.selectedCondominioId)
    }

    @Test
    fun `GIVEN condominio loaded WHEN selecting it THEN populates apartamentos list`() = runTest {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio(Condominio.dummy.id)

        val apartamentos = viewModel.uiState.value.apartamentos
        assertEquals(Condominio.dummy.apartamentos.size, apartamentos.size)
        assertEquals(Condominio.dummy.apartamentos.first().id, apartamentos.first().id)
    }

    @Test
    fun `GIVEN condominio with apartamentos WHEN selecting THEN summary stats are correct`() = runTest {
        val ocupado = Apartamento.dummy                          // has moradores
        val vago = Apartamento.dummy.copy(id = "vago", moradores = emptyList())
        val condominio = Condominio.dummy.copy(apartamentos = listOf(ocupado, vago))

        loadCondominio(condominio)
        viewModel.selectCondominio(condominio.id)

        val state = viewModel.uiState.value
        assertEquals(2, state.totalApartamentos)
        assertEquals(1, state.totalOcupados)
        assertEquals(1, state.totalVagos)
    }

    @Test
    fun `GIVEN apartment with moradores WHEN selecting condominio THEN isOcupado is true`() = runTest {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio(Condominio.dummy.id)

        val apt = viewModel.uiState.value.apartamentos.first()
        assertTrue(apt.isOcupado)
        assertEquals(Apartamento.dummy.moradores.size, apt.moradorCount)
    }

    @Test
    fun `GIVEN apartment with no moradores WHEN selecting condominio THEN isOcupado is false`() = runTest {
        val vago = Apartamento.dummy.copy(moradores = emptyList())
        val condominio = Condominio.dummy.copy(apartamentos = listOf(vago))
        loadCondominio(condominio)

        viewModel.selectCondominio(condominio.id)

        val apt = viewModel.uiState.value.apartamentos.first()
        assertFalse(apt.isOcupado)
        assertEquals(0, apt.moradorCount)
    }

    @Test
    fun `GIVEN unknown condominioId WHEN selecting THEN state does not change`() = runTest {
        loadCondominio(Condominio.dummy)

        viewModel.selectCondominio("unknown-id")

        assertNull(viewModel.uiState.value.selectedCondominioId)
        assertEquals(emptyList(), viewModel.uiState.value.apartamentos)
    }

    // --- Navigation events ---

    @Test
    fun `WHEN add apartamento clicked THEN navigation event is AddApartamento`() = runTest {
        loadCondominio(Condominio.dummy)
        viewModel.selectCondominio(Condominio.dummy.id)

        viewModel.onAddApartamentoClick()

        assertIs<DashboardNavigationEvent.AddApartamento>(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `WHEN apartamento clicked THEN navigation event is ApartamentoDetails with correct id`() = runTest {
        viewModel.onApartamentoClick("apt-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<DashboardNavigationEvent.ApartamentoDetails>(event)
        assertEquals("apt-123", event.apartamentoId)
    }

    @Test
    fun `GIVEN AddApartamento nav event WHEN handled THEN navigation event is cleared`() = runTest {
        loadCondominio(Condominio.dummy)
        viewModel.selectCondominio(Condominio.dummy.id)
        viewModel.onAddApartamentoClick()
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN ApartamentoDetails nav event WHEN handled THEN navigation event is cleared`() = runTest {
        viewModel.onApartamentoClick("apt-123")
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- Root-cause test: condominioId must be carried by the navigation event ---
    //
    // In the real app, CondominioDashboardNavHost handles DashboardNavigationEvent.AddApartamento
    // and calls navController.navigate(AddApartamentoRoute) — but it never calls
    // addApartamentoViewModel.setCondominioId(...).  The only way the nav host can know
    // WHICH condominio to pass is if the event itself carries the condominioId.
    //
    // Currently DashboardNavigationEvent.AddApartamento is a singleton `object` with no data,
    // so this test fails to compile:
    //   error: unresolved reference: condominioId

    @Test
    fun `WHEN add apartamento clicked with condominio selected THEN navigation event carries the condominioId`() = runTest {
        loadCondominio(Condominio.dummy)
        viewModel.selectCondominio(Condominio.dummy.id)

        viewModel.onAddApartamentoClick()

        val event = viewModel.uiState.value.navigationEvent
        assertIs<DashboardNavigationEvent.AddApartamento>(event)
        assertEquals(Condominio.dummy.id, event.condominioId) // compile error: unresolved reference 'condominioId'
    }

    // --- Stale data / refresh ---

    @Test
    fun `GIVEN condominio selected WHEN loadCondominios called after apartamento added THEN apartamento list is refreshed`() = runTest {
        val condominioId = Condominio.dummy.id
        val condominioEmpty = Condominio.dummy.copy(apartamentos = emptyList())

        loadCondominio(condominioEmpty)
        viewModel.selectCondominio(condominioId)
        assertEquals(0, viewModel.uiState.value.apartamentos.size)

        val newApt = Apartamento.dummy.copy(id = "new-apt", moradores = emptyList())
        val condominioWithApt = condominioEmpty.copy(apartamentos = listOf(newApt))
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(condominioWithApt))
        viewModel.loadCondominios()

        assertEquals(1, viewModel.uiState.value.apartamentos.size)
        assertEquals("new-apt", viewModel.uiState.value.apartamentos.first().id)
    }

    // --- Helpers ---

    private suspend fun loadCondominio(condominio: Condominio) {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(condominio))
        viewModel.loadCondominios()
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
