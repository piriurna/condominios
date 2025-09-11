package com.zalamena.condominios.pessoa.data.repository

import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity
import com.zalamena.condominios.pessoa.data.mapper.toDomain
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PessoaRepositoryTest: TestsWithMocks() {
    @Mock
    lateinit var pessoaDao: PessoaDao

    private val pessoaRepository: PessoaRepository by lazy { PessoaRepositoryImpl(pessoaDao) }

    @Test
    fun `GIVEN no pessoa is added WHEN getting all pessoas THEN return empty list`() = runTest {
        everySuspending { pessoaDao.getAllIndividos() } returns emptyList()


        val result = pessoaRepository.getAllIndividos()

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrNull())

    }


    @Test
    fun `GIVEN a pessoa is added WHEN getting all pessoas THEN return the added pessoa`() = runTest {
        val pessoaEntity = PessoaEntity.dummy

        everySuspending { pessoaDao.getAllIndividos() } returns listOf(pessoaEntity)

        val result = pessoaRepository.getAllIndividos()

        assertTrue(result.isSuccess)
        assertEquals(listOf(pessoaEntity.toDomain()), result.getOrNull())
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}