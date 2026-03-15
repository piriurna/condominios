package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.AddMoradorUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertTrue

class AddMoradorUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val addMoradorUseCase by lazy { AddMoradorUseCaseImpl(moradoresRepository) }

    @Test
    fun `GIVEN valid pessoa and apartamento WHEN adding morador THEN should succeed`() = runTest {
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId") } returns Result.success(Unit)

        val result = addMoradorUseCase("pessoaId", "apartamentoId")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN repository throws WHEN adding morador THEN should fail`() = runTest {
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId") } runs {
            throw MoradorException.DuplicateMoradorException
        }

        val result = addMoradorUseCase("pessoaId", "apartamentoId")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is MoradorException.DuplicateMoradorException)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
