package com.zalamena.condominios.condominio.data.condominio

import com.zalamena.condominios.condominio.data.condominio.dao.CondominioDao
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioWithAllData
import com.zalamena.condominios.condominio.data.condominio.repository.CondominioRepositoryImpl
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.Mocker
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
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

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}