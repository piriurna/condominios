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

class GetIndividuosListUseCaseTest: TestsWithMocks() {

    @Mock
    lateinit var individuoRepository: IndividuoRepository

    private val getIndividuosListUseCase by lazy { GetIndividuosListUseCase(individuoRepository) }



    @Test
    fun `GIVEN no user is added WHEN getting user list THEN it should return empty list`() = runTest {
        val newIndividuo = Individuo.dummy

        everySuspending { individuoRepository.getAllIndividos() } returns Result.success(emptyList())

        val getResult = getIndividuosListUseCase.invoke()

        assertTrue(getResult.isSuccess)
        assertEquals(emptyList(), getResult.getOrThrow())
    }


    @Test
    fun `GIVEN user is added WHEN getting user list THEN it should successfully get user`() = runTest {
        val newIndividuo = Individuo.dummy

        everySuspending { individuoRepository.getAllIndividos() } returns Result.success(listOf(newIndividuo))

        val addResult = getIndividuosListUseCase.invoke()

        assertTrue(addResult.isSuccess)
        assertEquals(listOf(newIndividuo),addResult.getOrThrow())
    }



    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}