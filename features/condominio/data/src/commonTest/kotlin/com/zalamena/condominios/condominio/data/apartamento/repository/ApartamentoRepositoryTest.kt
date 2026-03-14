package com.zalamena.condominios.condominio.data.apartamento.repository

import com.zalamena.condominios.condominio.data.apartamento.dao.ApartamentoDao
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoWithAllData
import com.zalamena.condominios.condominio.data.apartamento.mapper.toDomain
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApartamentoRepositoryTest: TestsWithMocks() {

    @Mock
    lateinit var apartamentoDao: ApartamentoDao

    private val apartamentoRepository by lazy {  ApartamentoRepositoryImpl(apartamentoDao) }


    @Test
    fun `GIVEN no apartamento added WHEN getting apartamentos THEN should return empty list`() = runTest {
        everySuspending { apartamentoDao.getApartamentos() } returns emptyList()


        val result = apartamentoRepository.getApartamentos()

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow())

    }


    @Test
    fun `GIVEN apartamento added WHEN getting apartamentos THEN should return list with apartamento`() = runTest {
        everySuspending { apartamentoDao.getApartamentos() } returns listOf(ApartamentoWithAllData.dummy)

        val result = apartamentoRepository.getApartamentos()

        assertTrue(result.isSuccess)
        assertEquals(listOf(ApartamentoEntity.dummy.toDomain()), result.getOrThrow())

    }

    @Test
    fun `GIVEN apartamento exists WHEN getting apartamento THEN should return apartamento`() =
        runTest {
            everySuspending { apartamentoDao.getApartamento("apartamentoId") } returns ApartamentoWithAllData.dummy

            val result = apartamentoRepository.getApartamento("apartamentoId")


            assertTrue(result.isSuccess)
            assertEquals(ApartamentoEntity.dummy.toDomain(), result.getOrThrow())

        }



    @Test
    fun `GIVEN apartamento does not exist WHEN getting apartamento THEN should return fail`() = runTest {

        everySuspending { apartamentoDao.getApartamento("apartamentoId") } returns null

        val result = apartamentoRepository.getApartamento("apartamentoId")


        assertTrue(result.isFailure)
        assertEquals(ApartamentoException.NoApartmentFoundException, result.exceptionOrNull())
    }



    @Test
    fun `GIVEN valid apartamento WHEN adding it THEN should be success`() = runTest {
        val condominioId = "condominioId"
        everySuspending { apartamentoDao.addApartamento(ApartamentoEntity.dummy) } returns Unit

        val result = apartamentoRepository.addApartamento(condominioId, ApartamentoEntity.dummy.toDomain())

        assertTrue(result.isSuccess)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}