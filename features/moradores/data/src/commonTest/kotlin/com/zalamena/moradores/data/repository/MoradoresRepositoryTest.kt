package com.zalamena.moradores.data.repository

import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoradoresRepositoryTest: TestsWithMocks() {

    @Mock
    lateinit var moradoresDao: MoradoresDao

    val moradorMapper = MoradorMapper()

    private val moradoresRepository: MoradoresRepository by lazy { MoradoresRepositoryImpl(moradoresDao, moradorMapper) }

    @Test
    fun `GIVEN no user is added WHEN trying to get all users THEN it should return an empty list of morador`() = runTest {
        every { moradoresDao.getAllMoradores() } returns emptyList()

        val result = moradoresRepository.getAllMoradores()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN a user is added WHEN trying to get all users THEN it should return a list with the morador`() = runTest {
        every { moradoresDao.getAllMoradores() } returns listOf(MoradorEntity.dummy)

        val result = moradoresRepository.getAllMoradores()

        assertTrue(result.isSuccess)
        assertEquals(1,result.getOrThrow().size)
    }


    @Test
    fun ` WHEN adding a valid user THEN it should success`() = runTest {
        val morador = Morador.dummy

        with(moradorMapper) {
            every { moradoresDao.addMorador(morador.toEntity()) } runs {}
        }

        val addResult = moradoresRepository.addMorador(morador)

        assertTrue(addResult.isSuccess)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}