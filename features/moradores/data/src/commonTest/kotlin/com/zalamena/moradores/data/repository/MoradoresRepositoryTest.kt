package com.zalamena.moradores.data.repository

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.data.entities.MoradorWithIndividuoAndApartamentoEntity
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.domain.models.MoradorException
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

    @Mock
    lateinit var apartamentoRepository: ApartamentosRepository

    val moradorMapper = MoradorMapper()

    private val moradoresRepository: MoradoresRepository by lazy {
        MoradoresRepositoryImpl(
            moradoresDao,
            apartamentoRepository,
            moradorMapper
        )
    }

    @Test
    fun `GIVEN no user is added WHEN trying to get all users THEN it should return an empty list of morador`() = runTest {
        everySuspending { moradoresDao.getAllMoradores() } returns emptyList()

        val result = moradoresRepository.getAllMoradores()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN a user is added WHEN trying to get all users THEN it should return a list with the morador`() = runTest {
        everySuspending { moradoresDao.getAllMoradores() } returns
                listOf(MoradorWithIndividuoAndApartamentoEntity.dummy)

        val result = moradoresRepository.getAllMoradores()

        assertTrue(result.isSuccess)
        assertEquals(1,result.getOrThrow().size)
    }


    @Test
    fun `GIVEN a valid individuo and valid apartamento when adding morador THEN it should success`() = runTest {
        val morador = MoradorEntity.dummy.copy(
            id = 0L,
            individuoId = Individuo.dummy.id,
            apartamentoId = Apartamento.dummy.id
        )

        with(moradorMapper) {
            everySuspending { moradoresDao.addMorador(morador) } runs {}
        }

        val addResult = moradoresRepository.addMorador(Individuo.dummy, Apartamento.dummy)

        assertTrue(addResult.isSuccess)
    }

    @Test
    fun `GIVEN no user is added with the same cpf WHEN trying to get a user THEN should return a failure with UserNotFoundException`() = runTest {
        val cpf = "cpf"
        val apartamentoId = "apartamentoId"
        everySuspending { moradoresDao.getMorador(cpf, apartamentoId) } returns null


        val result = moradoresRepository.getMorador(cpf, apartamentoId)


        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is MoradorException.MoradorNotFoundException)
    }


    @Test
    fun `GIVEN there is 1 morador in an apartment WHEN getting all moradores for apartment THEN should return a list with 1 morador`() = runTest {
        val apartamentoId = "apartamentoId"
        everySuspending { moradoresDao.getAllMoradoresForApartamento(apartamentoId) } returns
                listOf(MoradorWithIndividuoAndApartamentoEntity.dummy)

        val result = moradoresRepository.getAllMoradoresForApartamento(apartamentoId)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN there is no morador in an apartment WHEN getting all moradores for  apartment THEN should return empty list`() = runTest {
        val apartamentoId = "apartamentoId"
        everySuspending { moradoresDao.getAllMoradoresForApartamento(apartamentoId) } returns
                listOf()

        val result = moradoresRepository.getAllMoradoresForApartamento(apartamentoId)

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN there is 1 morador in an apartment WHEN getting apartamento with moradores THEN should return a correct apartment and a list with 1 morador`() = runTest {
        val apartamentoId = "apartamentoId"
        everySuspending { moradoresDao.getAllMoradoresForApartamento(apartamentoId) } returns
                listOf(MoradorWithIndividuoAndApartamentoEntity.dummy)

        everySuspending { apartamentoRepository.getApartamento(apartamentoId) } returns Result.success(Apartamento.dummy)

        val result = moradoresRepository.getApartamentoWithMoradores(apartamentoId)

        assertTrue(result.isSuccess)
        assertEquals(Apartamento.dummy, result.getOrThrow().apartamento)
        assertEquals(1, result.getOrThrow().moradores.size)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}