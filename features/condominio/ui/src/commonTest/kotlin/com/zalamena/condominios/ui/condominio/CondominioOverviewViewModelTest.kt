package com.zalamena.condominios.ui.condominio

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominioUseCase
import com.zalamena.condominios.condominio.ui.condominio.overview.CondominioOverviewViewModel
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CondominioOverviewViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val getCondominioUseCase by lazy { GetCondominioUseCase(condominioRepository) }
    private val viewModel by lazy { CondominioOverviewViewModel(getCondominioUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN isLoading should be false`() = runTest {
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN condominio should be null`() = runTest {
        assertNull(viewModel.uiState.value.condominio)
    }

    @Test
    fun `GIVEN condominio exists WHEN fetching data THEN should populate condominio`() = runTest {
        everySuspending { condominioRepository.getCondominio("id") } returns Result.success(Condominio.dummy)

        viewModel.fetchData("id")

        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isError)
        assertEquals(Condominio.dummy, viewModel.uiState.value.condominio)
    }

    @Test
    fun `GIVEN condominio fetch fails WHEN fetching data THEN should set error state`() = runTest {
        everySuspending { condominioRepository.getCondominio("id") } returns Result.failure(Exception("Not found"))

        viewModel.fetchData("id")

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.isError)
        assertNull(viewModel.uiState.value.condominio)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
