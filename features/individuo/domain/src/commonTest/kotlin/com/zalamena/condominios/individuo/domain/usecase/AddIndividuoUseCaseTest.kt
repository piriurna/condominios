package com.zalamena.condominios.individuo.domain.usecase

import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.models.IndividuoException
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddIndividuoUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var individuoRepository: IndividuoRepository

    private val addIndividuoUseCase by lazy { AddIndividuoUseCase(individuoRepository) }



    @Test
    fun `GIVEN no user with same cpf is added WHEN adding user THEN it should be successfully added`() = runTest {
        val newIndividuo = Individuo.dummy

        everySuspending { individuoRepository.getIndividuo(newIndividuo.cpf) } returns Result.failure(IndividuoException.IndividuoNotFoundException)
        everySuspending { individuoRepository.addIndividuo(newIndividuo) } returns Result.success(newIndividuo)

        val addResult = addIndividuoUseCase.invoke(newIndividuo)

        assertTrue(addResult.isSuccess)
    }


    @Test
    fun `GIVEN user with same cpf is found WHEN adding user THEN it should fail adding user`() = runTest {
        val newIndividuo = Individuo.dummy

        everySuspending { individuoRepository.getIndividuo(newIndividuo.cpf) } returns Result.success(newIndividuo)
        everySuspending { individuoRepository.addIndividuo(newIndividuo) } returns Result.success(newIndividuo)

        val addResult = addIndividuoUseCase.invoke(newIndividuo)

        assertTrue(addResult.isFailure)
        assertEquals(IndividuoException.DuplicateIndividuoException,addResult.exceptionOrNull())
        verifyWithSuspend {
            individuoRepository.getIndividuo(newIndividuo.cpf)
        }
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}