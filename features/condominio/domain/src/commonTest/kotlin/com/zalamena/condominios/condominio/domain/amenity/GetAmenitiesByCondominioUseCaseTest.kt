package com.zalamena.condominios.condominio.domain.amenity

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository
import com.zalamena.condominios.condominio.domain.amenity.usecase.GetAmenitiesByCondominioUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAmenitiesByCondominioUseCaseTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var amenityRepository: AmenityRepository

    private val useCase by lazy { GetAmenitiesByCondominioUseCase(amenityRepository) }

    @Test
    fun `GIVEN amenities exist WHEN invoke THEN returns list`() = runTest {
        val amenities = listOf(Amenity.dummy, Amenity.dummy.copy(id = "amenity-2", nome = "Churrasqueira"))
        everySuspending { amenityRepository.getAmenitiesByCondominio("condo-1") } returns Result.success(amenities)

        val result = useCase("condo-1")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN no amenities WHEN invoke THEN returns empty list`() = runTest {
        everySuspending { amenityRepository.getAmenitiesByCondominio("condo-1") } returns Result.success(emptyList())

        val result = useCase("condo-1")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN returns failure`() = runTest {
        everySuspending { amenityRepository.getAmenitiesByCondominio("condo-1") } returns Result.failure(Exception("error"))

        val result = useCase("condo-1")

        assertTrue(result.isFailure)
    }
}
