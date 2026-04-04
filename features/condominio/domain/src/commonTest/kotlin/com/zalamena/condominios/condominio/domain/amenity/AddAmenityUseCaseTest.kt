package com.zalamena.condominios.condominio.domain.amenity

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository
import com.zalamena.condominios.condominio.domain.amenity.usecase.AddAmenityUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertTrue

class AddAmenityUseCaseTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var amenityRepository: AmenityRepository

    private val useCase by lazy { AddAmenityUseCase(amenityRepository) }

    @Test
    fun `GIVEN valid amenity WHEN invoke THEN returns success`() = runTest {
        val amenity = Amenity.dummy
        everySuspending { amenityRepository.addAmenity(amenity) } returns Result.success(Unit)

        val result = useCase(amenity)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN blank nome WHEN invoke THEN returns failure`() = runTest {
        val amenity = Amenity.dummy.copy(nome = "")

        val result = useCase(amenity)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `GIVEN whitespace nome WHEN invoke THEN returns failure`() = runTest {
        val amenity = Amenity.dummy.copy(nome = "   ")

        val result = useCase(amenity)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `GIVEN blank condominioId WHEN invoke THEN returns failure`() = runTest {
        val amenity = Amenity.dummy.copy(condominioId = "")

        val result = useCase(amenity)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN returns failure`() = runTest {
        val amenity = Amenity.dummy
        everySuspending { amenityRepository.addAmenity(amenity) } returns Result.failure(Exception("DB error"))

        val result = useCase(amenity)

        assertTrue(result.isFailure)
    }
}
