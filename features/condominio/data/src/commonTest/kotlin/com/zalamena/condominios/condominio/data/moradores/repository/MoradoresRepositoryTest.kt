package com.zalamena.condominios.condominio.data.moradores.repository

import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertTrue

class MoradoresRepositoryTest: TestsWithMocks() {

    @Mock
    lateinit var moradoresDao: MoradoresDao

    @Mock
    lateinit var apartamentoRepository: ApartamentosRepository


    private val moradoresRepository: MoradoresRepository by lazy {
        MoradoresRepositoryImpl(
            moradoresDao,
        )
    }


    @Test
    fun `GIVEN a valid pessoa and valid apartamento when adding morador THEN it should success`() = runTest {
        val morador = MoradorEntity.dummy.copy(
            id = 0L,
            pessoaId = Pessoa.dummy.id,
            apartamentoId = Apartamento.dummy.id,
            tipo = MoradorTipo.RESIDENTE.name
        )

        everySuspending { moradoresDao.addMorador(morador) } runs {}

        val addResult = moradoresRepository.addMorador(Pessoa.dummy.id, Apartamento.dummy.id, MoradorTipo.RESIDENTE)

        assertTrue(addResult.isSuccess)
    }


    @Test
    fun `GIVEN no user is added with the same id WHEN trying to get a morador THEN should return a failure with MoradorNotFoundException`() = runTest {
        val id = "id"
        val apartamentoId = "apartamentoId"
        everySuspending { moradoresDao.getMorador(id, apartamentoId) } returns null


        val result = moradoresRepository.getMorador(id, apartamentoId)


        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is MoradorException.MoradorNotFoundException)
    }

    @Test
    fun `GIVEN pessoa is morador in apartment A WHEN adding same pessoa to apartment B THEN should succeed`() = runTest {
        val pessoaId = "pessoaId"
        val apartamentoAId = "apartamentoA"
        val apartamentoBId = "apartamentoB"

        val moradorForAptA = MoradorEntity(
            id = 0L,
            pessoaId = pessoaId,
            apartamentoId = apartamentoAId,
            tipo = MoradorTipo.RESIDENTE.name
        )

        val moradorForAptB = MoradorEntity(
            id = 0L,
            pessoaId = pessoaId,
            apartamentoId = apartamentoBId,
            tipo = MoradorTipo.RESIDENTE.name
        )

        // First insert (apartment A) succeeds
        everySuspending { moradoresDao.addMorador(moradorForAptA) } runs {}

        val resultA = moradoresRepository.addMorador(pessoaId, apartamentoAId, MoradorTipo.RESIDENTE)
        assertTrue(resultA.isSuccess, "Adding morador to apartment A should succeed")

        // Second insert (apartment B) should also succeed - different apartamentoId
        everySuspending { moradoresDao.addMorador(moradorForAptB) } runs {}

        val resultB = moradoresRepository.addMorador(pessoaId, apartamentoBId, MoradorTipo.RESIDENTE)
        assertTrue(resultB.isSuccess, "Adding same pessoa as morador to apartment B should succeed")

        // Verify both DAO calls were made
        verifyWithSuspend(exhaustive = false) {
            moradoresDao.addMorador(moradorForAptA)
            moradoresDao.addMorador(moradorForAptB)
        }
    }

    @Test
    fun `GIVEN pessoa is morador in apartment A WHEN adding to apartment B and DAO throws THEN should return failure`() = runTest {
        val pessoaId = "pessoaId"
        val apartamentoBId = "apartamentoB"

        val moradorForAptB = MoradorEntity(
            id = 0L,
            pessoaId = pessoaId,
            apartamentoId = apartamentoBId,
            tipo = MoradorTipo.RESIDENTE.name
        )

        // DAO throws an exception (simulating a constraint violation or other DB error)
        everySuspending { moradoresDao.addMorador(moradorForAptB) } runs {
            throw RuntimeException("UNIQUE constraint failed")
        }

        val result = moradoresRepository.addMorador(pessoaId, apartamentoBId, MoradorTipo.RESIDENTE)

        assertTrue(result.isFailure, "Should return failure when DAO throws")
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}