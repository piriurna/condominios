package com.zalamena.condominios.ui.amenity

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository
import com.zalamena.condominios.condominio.domain.amenity.usecase.GetAmenitiesByCondominioUseCase
import com.zalamena.condominios.condominio.ui.amenity.list.AmenityListNavigationEvent
import com.zalamena.condominios.condominio.ui.amenity.list.AmenityListViewModel
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
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AmenityListViewModelTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var amenityRepository: AmenityRepository

    private val getAmenitiesByCondominioUseCase by lazy { GetAmenitiesByCondominioUseCase(amenityRepository) }
    private val viewModel by lazy { AmenityListViewModel(getAmenitiesByCondominioUseCase) }

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
    fun `GIVEN initial state THEN amenities is empty`() {
        assertTrue(viewModel.uiState.value.amenities.isEmpty())
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `GIVEN amenities exist WHEN loadAmenities THEN list is populated`() = runTest(testDispatcher) {
        val amenities = listOf(Amenity.dummy, Amenity.dummy.copy(id = "2", nome = "Churrasqueira"))
        everySuspending { amenityRepository.getAmenitiesByCondominio("condo-1") } returns Result.success(amenities)

        viewModel.setCondominioId("condo-1")
        viewModel.loadAmenities()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.amenities.size)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `GIVEN no amenities WHEN loadAmenities THEN list is empty`() = runTest(testDispatcher) {
        everySuspending { amenityRepository.getAmenitiesByCondominio("condo-1") } returns Result.success(emptyList())

        viewModel.setCondominioId("condo-1")
        viewModel.loadAmenities()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.amenities.isEmpty())
    }

    @Test
    fun `GIVEN repository fails WHEN loadAmenities THEN error is set`() = runTest(testDispatcher) {
        everySuspending { amenityRepository.getAmenitiesByCondominio("condo-1") } returns Result.failure(Exception("fail"))

        viewModel.setCondominioId("condo-1")
        viewModel.loadAmenities()
        advanceUntilIdle()

        assertEquals("fail", viewModel.uiState.value.error)
    }

    @Test
    fun `GIVEN blank condominioId WHEN loadAmenities THEN nothing happens`() = runTest(testDispatcher) {
        viewModel.loadAmenities()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.amenities.isEmpty())
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `WHEN onAddAmenityClick THEN navigation event is set`() = runTest(testDispatcher) {
        viewModel.setCondominioId("condo-1")
        viewModel.onAddAmenityClick()

        assertIs<AmenityListNavigationEvent.AddAmenity>(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `WHEN onNavigationHandled THEN event is cleared`() = runTest(testDispatcher) {
        viewModel.setCondominioId("condo-1")
        viewModel.onAddAmenityClick()
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }
}
