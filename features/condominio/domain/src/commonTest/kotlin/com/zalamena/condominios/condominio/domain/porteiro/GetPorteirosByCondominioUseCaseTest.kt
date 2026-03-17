package com.zalamena.condominios.condominio.domain.porteiro

import com.zalamena.condominios.condominio.domain.porteiro.models.PorteiroInfo
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroProvider
import com.zalamena.condominios.condominio.domain.porteiro.usecase.GetPorteirosByCondominioUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetPorteirosByCondominioUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var porteiroProvider: PorteiroProvider

    private val useCase by lazy { GetPorteirosByCondominioUseCase(porteiroProvider) }

    @Test
    fun `GIVEN valid condominioId WHEN getting porteiros THEN returns list from provider`() = runTest {
        val porteiros = listOf(
            PorteiroInfo("p-1", "Porteiro 1", "111", "p1@test.com", "condo-1"),
            PorteiroInfo("p-2", "Porteiro 2", "222", "p2@test.com", "condo-1")
        )
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.success(porteiros)

        val result = useCase("condo-1")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN blank condominioId WHEN getting porteiros THEN returns failure`() = runTest {
        val result = useCase("")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN whitespace condominioId WHEN getting porteiros THEN returns failure`() = runTest {
        val result = useCase("   ")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN provider fails WHEN getting porteiros THEN propagates failure`() = runTest {
        val exception = Exception("DB error")
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.failure(exception)

        val result = useCase("condo-1")

        assertTrue(result.isFailure)
        assertEquals("DB error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `GIVEN provider returns empty list WHEN getting porteiros THEN returns empty list`() = runTest {
        everySuspending { porteiroProvider.getPorteirosByCondominioId("condo-1") } returns Result.success(emptyList())

        val result = useCase("condo-1")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
