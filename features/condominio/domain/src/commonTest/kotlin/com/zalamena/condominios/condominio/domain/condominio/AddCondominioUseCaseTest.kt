package com.zalamena.condominios.condominio.domain.condominio

import com.zalamena.condominios.condominio.domain.condominio.model.AddCondominioForm
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.AddCondominioUseCase
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddCondominioUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val addCondominioUseCase by lazy { AddCondominioUseCase(condominioRepository) }

    @Test
    fun `GIVEN no existing condominios WHEN adding THEN returns id with count 0`() = runTest {
        val form = AddCondominioForm.dummy
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.success(Unit)

        val result = addCondominioUseCase(form)

        assertTrue(result.isSuccess)
        assertEquals("0-${form.nome}", result.getOrThrow())
    }

    @Test
    fun `GIVEN one existing condominio WHEN adding THEN returns id with count 1`() = runTest {
        val form = AddCondominioForm.dummy
        everySuspending { condominioRepository.getCondominios() } returns Result.success(listOf(Condominio.dummy))
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.success(Unit)

        val result = addCondominioUseCase(form)

        assertTrue(result.isSuccess)
        assertEquals("1-${form.nome}", result.getOrThrow())
    }

    @Test
    fun `GIVEN addCondominio fails WHEN adding THEN failure is propagated`() = runTest {
        val form = AddCondominioForm.dummy
        val exception = Exception("DB error")
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.failure(exception)

        val result = addCondominioUseCase(form)

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
