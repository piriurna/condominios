package com.zalamena.condominios.condominio.domain.condominio

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMoradoresUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository


    private val getMoradoresUseCase: GetMoradoresUseCase by lazy {
        GetMoradoresUseCase(condominioRepository)
    }

    @Test
    fun `GIVEN no apartamento added WHEN getting moradores THEN should return empty list`() = runTest {
        val condominioId = "condominioId"

        everySuspending { condominioRepository.getCondominio(condominioId) } returns Result.success(Condominio.dummy.copy(apartamentos = emptyList()))


        val result = getMoradoresUseCase(condominioId)

        assertTrue(result.isSuccess)

        assertEquals(emptyList(), result.getOrThrow())
    }

    @Test
    fun `GIVEN no moradores added WHEN getting moradores THEN should return empty list`() = runTest {
        val condominioId = "condominioId"

        everySuspending { condominioRepository.getCondominio(condominioId) } returns Result.success(
            Condominio.dummy.copy(apartamentos = Condominio.dummy.apartamentos.map { it.copy(moradores = emptyList()) })
        )


        val result = getMoradoresUseCase(condominioId)

        assertTrue(result.isSuccess)

        assertEquals(emptyList(), result.getOrThrow())
    }

    @Test
    fun `GIVEN moradores added WHEN getting moradores THEN should return list with moradores`() = runTest {
        val condominioId = "condominioId"

        everySuspending { condominioRepository.getCondominio(condominioId) } returns Result.success(
            Condominio.dummy
        )


        val result = getMoradoresUseCase(condominioId)

        assertTrue(result.isSuccess)

        assertEquals(1, result.getOrThrow().size)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }


}