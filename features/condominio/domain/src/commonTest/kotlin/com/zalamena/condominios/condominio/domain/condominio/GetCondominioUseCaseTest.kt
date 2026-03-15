package com.zalamena.condominios.condominio.domain.condominio

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominioUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCondominioUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val getCondominioUseCase by lazy { GetCondominioUseCase(condominioRepository) }

    @Test
    fun `GIVEN condominio exists WHEN getting condominio THEN should return condominio`() = runTest {
        everySuspending { condominioRepository.getCondominio("id") } returns Result.success(Condominio.dummy)

        val result = getCondominioUseCase("id")

        assertTrue(result.isSuccess)
        assertEquals(Condominio.dummy, result.getOrThrow())
    }

    @Test
    fun `GIVEN condominio does not exist WHEN getting condominio THEN should fail`() = runTest {
        val exception = Exception("Condominio not found")
        everySuspending { condominioRepository.getCondominio("id") } returns Result.failure(exception)

        val result = getCondominioUseCase("id")

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
