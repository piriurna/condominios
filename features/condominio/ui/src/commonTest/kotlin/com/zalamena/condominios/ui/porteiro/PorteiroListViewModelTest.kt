package com.zalamena.condominios.ui.porteiro

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.domain.porteiro.models.PorteiroInfo
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroDeleter
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroProvider
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroReassigner
import com.zalamena.condominios.condominio.domain.porteiro.usecase.DeletePorteiroUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.GetPorteirosByCondominioUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.ReassignPorteiroUseCase
import com.zalamena.condominios.condominio.ui.porteiro.list.PorteiroListViewModel
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PorteiroListViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var porteiroProvider: PorteiroProvider

    @Mock
    lateinit var porteiroDeleter: PorteiroDeleter

    @Mock
    lateinit var porteiroReassigner: PorteiroReassigner

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getPorteirosByCondominioUseCase by lazy { GetPorteirosByCondominioUseCase(porteiroProvider) }
    private val deletePorteiroUseCase by lazy { DeletePorteiroUseCase(porteiroDeleter) }
    private val reassignPorteiroUseCase by lazy { ReassignPorteiroUseCase(porteiroReassigner, condominioRepository) }
    private val getCondominiosUseCase by lazy { GetCondominiosUseCase(condominioRepository) }

    private val viewModel by lazy {
        PorteiroListViewModel(
            getPorteirosByCondominioUseCase,
            deletePorteiroUseCase,
            reassignPorteiroUseCase,
            getCondominiosUseCase
        )
    }

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
    fun `GIVEN initial state WHEN observing THEN porteiros list is empty`() = runTest(testScheduler) {
        assertEquals(emptyList(), viewModel.uiState.value.porteiros)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN isError is false`() = runTest(testScheduler) {
        assertFalse(viewModel.uiState.value.isError)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no dialogs shown`() = runTest(testScheduler) {
        assertNull(viewModel.uiState.value.showDeleteConfirmation)
        assertNull(viewModel.uiState.value.showReassignDialog)
    }

    // --- loadPorteiros ---

    @Test
    fun `GIVEN condominioId set WHEN loading THEN populates porteiros`() = runTest(testScheduler) {
        val porteiros = listOf(
            PorteiroInfo("p-1", "Porteiro 1", "111", "p1@test.com", "condo-1")
        )
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.success(porteiros)

        viewModel.setCondominioId("condo-1")
        viewModel.loadPorteiros()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.porteiros.size)
        assertEquals("Porteiro 1", state.porteiros.first().name)
    }

    @Test
    fun `GIVEN fetch fails WHEN loading THEN sets error state`() = runTest(testScheduler) {
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.failure(Exception("error"))

        viewModel.setCondominioId("condo-1")
        viewModel.loadPorteiros()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
    }

    @Test
    fun `GIVEN no condominioId WHEN loading THEN does nothing`() = runTest(testScheduler) {
        viewModel.loadPorteiros()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN no porteiros for condominio WHEN loading THEN returns empty list`() = runTest(testScheduler) {
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.success(emptyList())

        viewModel.setCondominioId("condo-1")
        viewModel.loadPorteiros()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.porteiros.isEmpty())
    }

    // --- Delete flow ---

    @Test
    fun `WHEN onDeleteClick THEN showDeleteConfirmation is set`() = runTest(testScheduler) {
        loadPorteiros()

        val porteiro = viewModel.uiState.value.porteiros.first()
        viewModel.onDeleteClick(porteiro)

        assertNotNull(viewModel.uiState.value.showDeleteConfirmation)
        assertEquals(porteiro.id, viewModel.uiState.value.showDeleteConfirmation?.id)
    }

    @Test
    fun `WHEN onDismissDelete THEN showDeleteConfirmation is cleared`() = runTest(testScheduler) {
        loadPorteiros()

        viewModel.onDeleteClick(viewModel.uiState.value.porteiros.first())
        viewModel.onDismissDelete()

        assertNull(viewModel.uiState.value.showDeleteConfirmation)
    }

    @Test
    fun `GIVEN delete succeeds WHEN onConfirmDelete THEN list is refreshed`() = runTest(testScheduler) {
        val porteiros = listOf(
            PorteiroInfo("p-1", "Porteiro 1", "11111111111", "p1@test.com", "condo-1")
        )
        var providerResponse: Result<List<PorteiroInfo>> = Result.success(porteiros)
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } runs { providerResponse }

        viewModel.setCondominioId("condo-1")
        viewModel.loadPorteiros()
        advanceUntilIdle()

        val porteiro = viewModel.uiState.value.porteiros.first()
        viewModel.onDeleteClick(porteiro)

        everySuspending { porteiroDeleter.deletePorteiro("p-1") } returns Result.success(Unit)
        providerResponse = Result.success(emptyList())

        viewModel.onConfirmDelete()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.showDeleteConfirmation)
        assertTrue(viewModel.uiState.value.porteiros.isEmpty())
    }

    // --- Reassign flow ---

    @Test
    fun `WHEN onReassignClick THEN showReassignDialog is set and condominios are loaded`() = runTest(testScheduler) {
        loadPorteiros()

        val otherCondominio = Condominio.dummy.copy(id = "condo-2", nome = "Other")
        everySuspending { condominioRepository.getCondominios() } returns Result.success(
            listOf(Condominio.dummy.copy(id = "condo-1"), otherCondominio)
        )

        val porteiro = viewModel.uiState.value.porteiros.first()
        viewModel.onReassignClick(porteiro)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.showReassignDialog)
        assertEquals(1, state.availableCondominios.size)
        assertEquals("condo-2", state.availableCondominios.first().id)
    }

    @Test
    fun `WHEN onDismissReassign THEN dialog is cleared`() = runTest(testScheduler) {
        loadPorteiros()

        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())

        viewModel.onReassignClick(viewModel.uiState.value.porteiros.first())
        advanceUntilIdle()

        viewModel.onDismissReassign()

        assertNull(viewModel.uiState.value.showReassignDialog)
        assertTrue(viewModel.uiState.value.availableCondominios.isEmpty())
    }

    @Test
    fun `GIVEN reassign succeeds WHEN onConfirmReassign THEN list is refreshed`() = runTest(testScheduler) {
        val porteiros = listOf(
            PorteiroInfo("p-1", "Porteiro 1", "11111111111", "p1@test.com", "condo-1")
        )
        var providerResponse: Result<List<PorteiroInfo>> = Result.success(porteiros)
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } runs { providerResponse }

        viewModel.setCondominioId("condo-1")
        viewModel.loadPorteiros()
        advanceUntilIdle()

        everySuspending { condominioRepository.getCondominios() } returns Result.success(
            listOf(Condominio.dummy.copy(id = "condo-1"), Condominio.dummy.copy(id = "condo-2"))
        )

        viewModel.onReassignClick(viewModel.uiState.value.porteiros.first())
        advanceUntilIdle()

        everySuspending { condominioRepository.getCondominio("condo-2") } returns Result.success(Condominio.dummy.copy(id = "condo-2"))
        everySuspending { porteiroReassigner.reassignPorteiro("p-1", "condo-2") } returns Result.success(Unit)
        providerResponse = Result.success(emptyList())

        viewModel.onConfirmReassign("condo-2")
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.showReassignDialog)
        assertTrue(viewModel.uiState.value.porteiros.isEmpty())
    }

    // --- Helpers ---

    private suspend fun loadPorteiros() {
        val porteiros = listOf(
            PorteiroInfo("p-1", "Porteiro 1", "11111111111", "p1@test.com", "condo-1")
        )
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.success(porteiros)

        viewModel.setCondominioId("condo-1")
        viewModel.loadPorteiros()
        testScheduler.advanceUntilIdle()
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
