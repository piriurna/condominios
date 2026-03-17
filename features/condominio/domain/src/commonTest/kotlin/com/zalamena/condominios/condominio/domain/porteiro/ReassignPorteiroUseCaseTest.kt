package com.zalamena.condominios.condominio.domain.porteiro

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroReassigner
import com.zalamena.condominios.condominio.domain.porteiro.usecase.ReassignPorteiroUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ReassignPorteiroUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var porteiroReassigner: PorteiroReassigner

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val useCase by lazy { ReassignPorteiroUseCase(porteiroReassigner, condominioRepository) }

    @Test
    fun `GIVEN valid ids and condominio exists WHEN reassigning THEN delegates to reassigner`() = runTest {
        everySuspending { condominioRepository.getCondominio("condo-2") } returns Result.success(Condominio.dummy)
        everySuspending { porteiroReassigner.reassignPorteiro("p-1", "condo-2") } returns Result.success(Unit)

        val result = useCase("p-1", "condo-2")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN blank porteiroId WHEN reassigning THEN returns failure`() = runTest {
        val result = useCase("", "condo-2")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN blank newCondominioId WHEN reassigning THEN returns failure`() = runTest {
        val result = useCase("p-1", "")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN condominio does not exist WHEN reassigning THEN returns failure`() = runTest {
        everySuspending { condominioRepository.getCondominio("nonexistent") } returns Result.failure(Exception("Not found"))

        val result = useCase("p-1", "nonexistent")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN reassigner fails WHEN reassigning THEN propagates failure`() = runTest {
        everySuspending { condominioRepository.getCondominio("condo-2") } returns Result.success(Condominio.dummy)
        val exception = Exception("Reassign failed")
        everySuspending { porteiroReassigner.reassignPorteiro("p-1", "condo-2") } returns Result.failure(exception)

        val result = useCase("p-1", "condo-2")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "Reassign failed")
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
