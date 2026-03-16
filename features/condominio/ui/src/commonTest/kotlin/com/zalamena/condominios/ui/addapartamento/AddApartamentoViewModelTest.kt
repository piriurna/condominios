package com.zalamena.condominios.ui.addapartamento

import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.AddApartamentoUseCase
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
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
class AddApartamentoViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val addApartamentoUseCase by lazy { AddApartamentoUseCase(apartamentosRepository) }
    private val viewModel by lazy { AddApartamentoViewModel(addApartamentoUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN form should be blank`() = runTest(testScheduler) {
        assertNull(viewModel.uiState.value.createdApartamentoId)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN numero set WHEN setting numero THEN state should update with digits only`() = runTest(testScheduler) {
        viewModel.setNumeroApartamento("101abc")

        assertEquals("101", viewModel.uiState.value.addApartamentoForm.numero)
    }

    @Test
    fun `GIVEN andar set WHEN setting andar THEN state should update with digits only`() = runTest(testScheduler) {
        viewModel.setAndarApartamento("3xyz")

        assertEquals("3", viewModel.uiState.value.addApartamentoForm.andar)
    }

    @Test
    fun `GIVEN condominioId set WHEN setting condominioId THEN state should update`() = runTest(testScheduler) {
        viewModel.setCondominioId("condominioId")

        assertEquals("condominioId", viewModel.uiState.value.condominioId)
    }

    @Test
    fun `GIVEN invalid form WHEN adding apartamento THEN should show error`() = runTest(testScheduler) {
        viewModel.addApartamento()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN valid form and repository succeeds WHEN adding apartamento THEN should set createdApartamentoId`() = runTest(testScheduler) {
        viewModel.setCondominioId("condominioId")
        viewModel.setNumeroApartamento("101")
        viewModel.setAndarApartamento("1")

        everySuspending { apartamentosRepository.addApartamento(isAny(), isAny()) } returns Result.success("newId")

        viewModel.addApartamento()
        advanceUntilIdle()

        assertEquals("newId", viewModel.uiState.value.createdApartamentoId)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN valid form and repository fails WHEN adding apartamento THEN should show error`() = runTest(testScheduler) {
        viewModel.setCondominioId("condominioId")
        viewModel.setNumeroApartamento("101")
        viewModel.setAndarApartamento("1")

        everySuspending { apartamentosRepository.addApartamento(isAny(), isAny()) } returns Result.failure(ApartamentoException.DuplicatedApartmentException)

        viewModel.addApartamento()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.createdApartamentoId)
        assertNotNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
