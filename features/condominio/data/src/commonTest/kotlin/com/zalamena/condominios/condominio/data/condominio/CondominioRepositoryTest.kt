package com.zalamena.condominios.condominio.data.condominio

import com.zalamena.condominios.condominio.data.condominio.dao.CondominioDao
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioWithAllData
import com.zalamena.condominios.condominio.data.condominio.entity.EnderecoEntity
import com.zalamena.condominios.condominio.data.condominio.mapper.toEntity
import com.zalamena.condominios.condominio.data.condominio.repository.CondominioRepositoryImpl
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CondominioRepositoryTest: TestsWithMocks() {

    @Mock
    lateinit var condominioDao: CondominioDao

    private val condominioRepository: CondominioRepository by lazy { CondominioRepositoryImpl(condominioDao) }

    @Test
    fun `GIVEN condominio is added WHEN getting a condominioById THEN return stored condominio`() = runTest {
        val condominioId = "id"
        everySuspending { condominioDao.getCondominio(condominioId) } returns CondominioWithAllData.dummy

        val result = condominioRepository.getCondominio(condominioId)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN condominio does not exist WHEN getting condominio THEN should fail`() = runTest {
        everySuspending { condominioDao.getCondominio("unknown") } returns null

        val result = condominioRepository.getCondominio("unknown")

        assertTrue(result.isFailure)
        assertEquals("Condominio not found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `GIVEN new endereco WHEN adding condominio THEN should insert endereco and condominio`() = runTest {
        val condominio = Condominio.dummy
        val enderecoId = condominio.endereco.toEntity().id

        everySuspending { condominioDao.getEndereco(enderecoId) } returns null
        everySuspending { condominioDao.insertEndereco(isAny()) } returns Unit
        everySuspending { condominioDao.insertCondominio(isAny()) } returns Unit

        val result = condominioRepository.addCondominio(condominio)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN existing endereco WHEN adding condominio THEN should reuse endereco and insert condominio`() = runTest {
        val condominio = Condominio.dummy
        val enderecoEntity = condominio.endereco.toEntity()

        everySuspending { condominioDao.getEndereco(enderecoEntity.id) } returns EnderecoEntity.dummy
        everySuspending { condominioDao.insertCondominio(isAny()) } returns Unit

        val result = condominioRepository.addCondominio(condominio)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN condominios exist WHEN getting all condominios THEN should return list`() = runTest {
        everySuspending { condominioDao.getCondominios() } returns listOf(CondominioWithAllData.dummy)

        val result = condominioRepository.getCondominios()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN no condominios WHEN getting all condominios THEN should return empty list`() = runTest {
        everySuspending { condominioDao.getCondominios() } returns emptyList()

        val result = condominioRepository.getCondominios()

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow())
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}