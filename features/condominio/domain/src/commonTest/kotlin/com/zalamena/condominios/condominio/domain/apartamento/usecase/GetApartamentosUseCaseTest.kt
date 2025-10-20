package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetApartamentosUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val getApartamentosUseCase by lazy { GetApartamentosUseCase(apartamentosRepository) }


    @Test
    fun `GIVEN no apartamento is found WHEN getting apartamentos THEN should return empty list`() = runTest {
        everySuspending { apartamentosRepository.getApartamentos() } returns Result.success(emptyList())


        val result = getApartamentosUseCase()


        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow())
    }

    @Test
    fun `GIVEN apartamento is found WHEN getting apartamentos THEN should return list with apartamento`() = runTest {
        everySuspending { apartamentosRepository.getApartamentos() } returns Result.success(
            listOf(Apartamento.dummy)
        )


        val result = getApartamentosUseCase()


        assertTrue(result.isSuccess)
        assertEquals(listOf(Apartamento.dummy), result.getOrThrow())

    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}