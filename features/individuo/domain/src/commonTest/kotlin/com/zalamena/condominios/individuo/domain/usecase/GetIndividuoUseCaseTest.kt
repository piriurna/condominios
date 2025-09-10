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

class GetIndividuoUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var individuoRepository: IndividuoRepository

    private val getIndividuoUseCase by lazy { GetIndividuoUseCase(individuoRepository) }



    @Test
    fun `GIVEN no user with same cpf is added WHEN getting user THEN it should fail getting user`() = runTest {
        val newIndividuo = Individuo.dummy

        everySuspending { individuoRepository.getIndividuo(newIndividuo.cpf) } returns Result.failure(IndividuoException.IndividuoNotFoundException)

        val getResult = getIndividuoUseCase.invoke(newIndividuo.cpf)

        assertTrue(getResult.isFailure)
        assertEquals(IndividuoException.IndividuoNotFoundException, getResult.exceptionOrNull())
    }


    @Test
    fun `GIVEN user with same cpf is found WHEN getting user THEN it should successfully get user`() = runTest {
        val newIndividuo = Individuo.dummy

        everySuspending { individuoRepository.getIndividuo(newIndividuo.cpf) } returns Result.success(newIndividuo)

        val addResult = getIndividuoUseCase.invoke(newIndividuo.cpf)

        assertTrue(addResult.isSuccess)
        assertEquals(newIndividuo,addResult.getOrThrow())
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}