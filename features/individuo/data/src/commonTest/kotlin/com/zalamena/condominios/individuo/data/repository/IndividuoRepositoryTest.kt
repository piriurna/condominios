package com.zalamena.condominios.individuo.data.repository

import com.zalamena.condominios.individuo.data.dao.IndividuoDao
import com.zalamena.condominios.individuo.data.entities.IndividuoEntity
import com.zalamena.condominios.individuo.data.mapper.toDomain
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IndividuoRepositoryTest: TestsWithMocks() {
    @Mock
    lateinit var individuoDao: IndividuoDao

    private val individuoRepository: IndividuoRepository by lazy { IndividuoRepositoryImpl(individuoDao) }

    @Test
    fun `GIVEN no individuo is added WHEN getting all individuos THEN return empty list`() = runTest {
        everySuspending { individuoDao.getAllIndividos() } returns emptyList()


        val result = individuoRepository.getAllIndividos()

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrNull())

    }


    @Test
    fun `GIVEN a individuo is added WHEN getting all individuos THEN return the added individuo`() = runTest {
        val individuoEntity = IndividuoEntity.dummy

        everySuspending { individuoDao.getAllIndividos() } returns listOf(individuoEntity)

        val result = individuoRepository.getAllIndividos()

        assertTrue(result.isSuccess)
        assertEquals(listOf(individuoEntity.toDomain()), result.getOrNull())
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}