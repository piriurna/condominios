package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.RemoveMoradorUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RemoveMoradorUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val removeMoradorUseCase by lazy { RemoveMoradorUseCaseImpl(moradoresRepository) }

    @Test
    fun `GIVEN blank pessoaId WHEN removing THEN should fail with IllegalArgumentException`() = runTest {
        val result = removeMoradorUseCase("", "apartamentoId")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN blank apartamentoId WHEN removing THEN should fail with IllegalArgumentException`() = runTest {
        val result = removeMoradorUseCase("pessoaId", "")

        assertTrue(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN valid IDs WHEN removing THEN should delegate to repository`() = runTest {
        everySuspending { moradoresRepository.removeMorador("pessoaId", "apartamentoId") } returns Result.success(Unit)

        val result = removeMoradorUseCase("pessoaId", "apartamentoId")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN repository fails WHEN removing THEN should propagate failure`() = runTest {
        everySuspending { moradoresRepository.removeMorador("pessoaId", "apartamentoId") } returns Result.failure(Exception("DB error"))

        val result = removeMoradorUseCase("pessoaId", "apartamentoId")

        assertTrue(result.isFailure)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
