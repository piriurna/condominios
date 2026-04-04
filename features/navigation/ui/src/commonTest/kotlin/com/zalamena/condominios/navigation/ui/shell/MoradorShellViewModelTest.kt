package com.zalamena.condominios.navigation.ui.shell

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominioUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mock
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MoradorShellViewModelTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val getCondominioUseCase by lazy { GetCondominioUseCase(condominioRepository) }
    private val viewModel by lazy { MoradorShellViewModel(getCondominioUseCase) }

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state THEN condominioName is empty and isLoading is false`() {
        val state = viewModel.uiState.value
        assertEquals("", state.condominioName)
        assertTrue(!state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `GIVEN condominioId set WHEN loadCondominio THEN condominioName is updated`() = runTest {
        val condominio = Condominio.dummy.copy(id = "condo-1", nome = "Residencial Sol")
        everySuspending { condominioRepository.getCondominio("condo-1") } returns Result.success(condominio)

        viewModel.setCondominioId("condo-1")
        viewModel.loadCondominio()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Residencial Sol", state.condominioName)
        assertTrue(!state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `GIVEN condominioId set WHEN loadCondominio fails THEN error is set`() = runTest {
        everySuspending { condominioRepository.getCondominio("condo-1") } returns Result.failure(Exception("Not found"))

        viewModel.setCondominioId("condo-1")
        viewModel.loadCondominio()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.condominioName)
        assertTrue(!state.isLoading)
        assertEquals("Not found", state.error)
    }

    @Test
    fun `GIVEN no condominioId set WHEN loadCondominio THEN nothing happens`() = runTest {
        viewModel.loadCondominio()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.condominioName)
        assertTrue(!state.isLoading)
        assertNull(state.error)
    }
}
