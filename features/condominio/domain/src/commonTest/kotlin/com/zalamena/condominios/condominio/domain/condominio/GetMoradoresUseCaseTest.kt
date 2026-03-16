package com.zalamena.condominios.condominio.domain.condominio

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMoradoresUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val getMoradoresUseCase: GetMoradoresUseCase by lazy {
        GetMoradoresUseCase(moradoresRepository)
    }

    @Test
    fun `GIVEN no moradores WHEN getting moradores for condominio THEN should return empty list`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condominioId") } returns Result.success(emptyList())

        val result = getMoradoresUseCase("condominioId")

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow())
    }

    @Test
    fun `GIVEN moradores exist WHEN getting moradores for condominio THEN should return list with correct tipos`() = runTest {
        val moradores = listOf(
            Morador.dummy.copy(tipo = MoradorTipo.PROPRIETARIO),
            Morador.dummy.copy(tipo = MoradorTipo.RESIDENTE_TEMPORARIO)
        )
        everySuspending { moradoresRepository.getMoradoresForCondominio("condominioId") } returns Result.success(moradores)

        val result = getMoradoresUseCase("condominioId")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
        assertEquals(MoradorTipo.PROPRIETARIO, result.getOrThrow()[0].tipo)
        assertEquals(MoradorTipo.RESIDENTE_TEMPORARIO, result.getOrThrow()[1].tipo)
    }

    @Test
    fun `GIVEN repository fails WHEN getting moradores THEN should propagate failure`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condominioId") } returns Result.failure(Exception("DB error"))

        val result = getMoradoresUseCase("condominioId")

        assertTrue(result.isFailure)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
