package com.zalamena.condominios.condominio.domain.condominio

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCondominiosUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val getCondominiosUseCase by lazy { GetCondominiosUseCase(condominioRepository) }

    @Test
    fun `GIVEN condominios exist WHEN getting condominios THEN should return list`() = runTest {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(Condominio.dummy))

        val result = getCondominiosUseCase()

        assertTrue(result.isSuccess)
        assertEquals(listOf(Condominio.dummy), result.getOrThrow())
    }

    @Test
    fun `GIVEN no condominios WHEN getting condominios THEN should return empty list`() = runTest {
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())

        val result = getCondominiosUseCase()

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow())
    }

    @Test
    fun `GIVEN repository fails WHEN getting condominios THEN should propagate failure`() = runTest {
        val exception = Exception("DB error")
        everySuspending { condominioRepository.getCondominios() } returns Result.failure(exception)

        val result = getCondominiosUseCase()

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
