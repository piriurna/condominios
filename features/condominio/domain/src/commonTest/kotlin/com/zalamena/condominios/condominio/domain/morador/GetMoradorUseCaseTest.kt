package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMoradorUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val getMoradorUseCase by lazy { GetMoradorUseCase(moradoresRepository) }

    @Test
    fun `GIVEN morador exists WHEN getting morador THEN should return morador`() = runTest {
        everySuspending { moradoresRepository.getMorador("cpf", "apartamentoId") } returns Result.success(Morador.dummy)

        val result = getMoradorUseCase("cpf", "apartamentoId")

        assertTrue(result.isSuccess)
        assertEquals(Morador.dummy, result.getOrThrow())
    }

    @Test
    fun `GIVEN morador does not exist WHEN getting morador THEN should fail`() = runTest {
        everySuspending { moradoresRepository.getMorador("cpf", "apartamentoId") } returns Result.failure(MoradorException.MoradorNotFoundException)

        val result = getMoradorUseCase("cpf", "apartamentoId")

        assertTrue(result.isFailure)
        assertEquals("Morador não encontrado", result.exceptionOrNull()?.message)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
