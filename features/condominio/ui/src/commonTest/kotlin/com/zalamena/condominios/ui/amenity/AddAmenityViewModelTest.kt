package com.zalamena.condominios.ui.amenity

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository
import com.zalamena.condominios.condominio.domain.amenity.usecase.AddAmenityUseCase
import com.zalamena.condominios.condominio.ui.amenity.add.AddAmenityNavigationEvent
import com.zalamena.condominios.condominio.ui.amenity.add.AddAmenityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddAmenityViewModelTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var amenityRepository: AmenityRepository

    private val addAmenityUseCase by lazy { AddAmenityUseCase(amenityRepository) }
    private val viewModel by lazy { AddAmenityViewModel(addAmenityUseCase) }

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
    fun `GIVEN initial state THEN all fields are empty`() {
        val state = viewModel.uiState.value
        assertEquals("", state.nome)
        assertEquals("", state.descricao)
        assertEquals("", state.capacidade)
        assertEquals("", state.precoUso)
        assertFalse(state.requerAprovacaoSindico)
        assertNull(state.navigationEvent)
    }

    @Test
    fun `WHEN setNome THEN nome is updated`() = runTest(testDispatcher) {
        viewModel.setNome("Salao")
        assertEquals("Salao", viewModel.uiState.value.nome)
    }

    @Test
    fun `WHEN setCapacidade with non-digits THEN non-digits are filtered`() = runTest(testDispatcher) {
        viewModel.setCapacidade("5a0b")
        assertEquals("50", viewModel.uiState.value.capacidade)
    }

    @Test
    fun `WHEN setPrecoUso THEN only digits are kept`() = runTest(testDispatcher) {
        viewModel.setPrecoUso("200.50")
        assertEquals("20050", viewModel.uiState.value.precoUso)
    }

    @Test
    fun `GIVEN blank nome WHEN submit THEN nomeError is set`() = runTest(testDispatcher) {
        viewModel.setCondominioId("condo-1")
        viewModel.setNome("")
        viewModel.submit()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.nomeError)
        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN valid form WHEN submit succeeds THEN navigation event is Success`() = runTest(testDispatcher) {
        everySuspending { amenityRepository.addAmenity(isAny()) } returns Result.success(Unit)

        viewModel.setCondominioId("condo-1")
        viewModel.setNome("Salao de Festas")
        viewModel.setPrecoUso("20000")
        viewModel.submit()
        advanceUntilIdle()

        assertIs<AddAmenityNavigationEvent.Success>(viewModel.uiState.value.navigationEvent)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN valid form WHEN submit fails THEN errorMessage is set`() = runTest(testDispatcher) {
        everySuspending { amenityRepository.addAmenity(isAny()) } returns Result.failure(Exception("DB error"))

        viewModel.setCondominioId("condo-1")
        viewModel.setNome("Salao de Festas")
        viewModel.submit()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `WHEN reset THEN state returns to initial`() = runTest(testDispatcher) {
        viewModel.setNome("Salao")
        viewModel.setDescricao("Desc")
        viewModel.reset()

        assertEquals("", viewModel.uiState.value.nome)
        assertEquals("", viewModel.uiState.value.descricao)
    }

    @Test
    fun `WHEN onNavigationHandled THEN navigationEvent is null`() = runTest(testDispatcher) {
        everySuspending { amenityRepository.addAmenity(isAny()) } returns Result.success(Unit)

        viewModel.setCondominioId("condo-1")
        viewModel.setNome("Salao")
        viewModel.submit()
        advanceUntilIdle()

        viewModel.onNavigationHandled()
        assertNull(viewModel.uiState.value.navigationEvent)
    }
}
