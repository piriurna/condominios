package com.zalamena.condominios.ui.adminhome

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeNavigationEvent
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeViewModel
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
class AdminHomeViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getCondominiosUseCase by lazy { GetCondominiosUseCase(condominioRepository) }
    private val viewModel by lazy { AdminHomeViewModel(getCondominiosUseCase) }

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
    fun `GIVEN initial state THEN condominios empty`() = runTest(testScheduler) {
        assertEquals(emptyList(), viewModel.uiState.value.condominios)
    }

    @Test
    fun `GIVEN initial state THEN is loading`() = runTest(testScheduler) {
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN initial state THEN no error`() = runTest(testScheduler) {
        assertFalse(viewModel.uiState.value.isError)
    }

    @Test
    fun `GIVEN initial state THEN no navigation event`() = runTest(testScheduler) {
        assertNull(viewModel.uiState.value.navigationEvent)
    }

    // --- loadCondominios ---

    @Test
    fun `GIVEN condominios exist WHEN loadCondominios THEN populates list`() = runTest(testScheduler) {
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
    fun `GIVEN no condominios WHEN loadCondominios THEN list is empty`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())

        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(emptyList(), state.condominios)
    }

    @Test
    fun `GIVEN fetch fails WHEN loadCondominios THEN isError is true`() = runTest(testScheduler) {
        everySuspending { condominioRepository.getCondominios() } returns Result.failure(Exception("DB error"))

        viewModel.loadCondominios()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertEquals(emptyList(), state.condominios)
    }

    // --- Navigation events ---

    @Test
    fun `WHEN condominio clicked THEN nav event is CondominioDetails with correct id`() = runTest(testScheduler) {
        viewModel.onCondominioClick("condo-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<AdminHomeNavigationEvent.CondominioDetails>(event)
        assertEquals("condo-123", event.condominioId)
    }

    @Test
    fun `WHEN add clicked THEN nav event is AddCondominio`() = runTest(testScheduler) {
        viewModel.onAddCondominioClick()

        assertIs<AdminHomeNavigationEvent.AddCondominio>(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN nav event WHEN handled THEN nav event is cleared`() = runTest(testScheduler) {
        viewModel.onAddCondominioClick()
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
