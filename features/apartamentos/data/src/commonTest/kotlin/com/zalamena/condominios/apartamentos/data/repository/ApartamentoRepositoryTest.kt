package com.zalamena.condominios.apartamentos.data.repository

import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.entity.ApartamentoEntity
import com.zalamena.condominios.apartamentos.data.mapper.toDomain
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
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
    fun `GIVEN apartamento exists WHEN getting apartamento THEN should return apartamento`() =
        runTest {
            everySuspending { apartamentoDao.getApartamento("apartamentoId") } returns ApartamentoEntity.dummy

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
        everySuspending { apartamentoDao.addApartamento(ApartamentoEntity.dummy) } returns Unit

        val result = apartamentoRepository.addApartamento(ApartamentoEntity.dummy.toDomain())

        assertTrue(result.isSuccess)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}