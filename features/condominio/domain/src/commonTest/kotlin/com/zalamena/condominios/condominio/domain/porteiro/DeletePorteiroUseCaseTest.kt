package com.zalamena.condominios.condominio.domain.porteiro

import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroDeleter
import com.zalamena.condominios.condominio.domain.porteiro.usecase.DeletePorteiroUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DeletePorteiroUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var porteiroDeleter: PorteiroDeleter

    private val useCase by lazy { DeletePorteiroUseCase(porteiroDeleter) }

    @Test
    fun `GIVEN valid porteiroId WHEN deleting THEN delegates to deleter`() = runTest {
        everySuspending { porteiroDeleter.deletePorteiro("p-1") } returns Result.success(Unit)

        val result = useCase("p-1")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN blank porteiroId WHEN deleting THEN returns failure`() = runTest {
        val result = useCase("")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN whitespace porteiroId WHEN deleting THEN returns failure`() = runTest {
        val result = useCase("   ")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN deleter fails WHEN deleting THEN propagates failure`() = runTest {
        val exception = Exception("Delete failed")
        everySuspending { porteiroDeleter.deletePorteiro("p-1") } returns Result.failure(exception)

        val result = useCase("p-1")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "Delete failed")
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
