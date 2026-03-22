package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.AddMoradorUseCaseImpl
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AddMoradorUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val addMoradorUseCase by lazy { AddMoradorUseCaseImpl(moradoresRepository) }

    @Test
    fun `GIVEN valid pessoa and apartamento WHEN adding RESIDENTE THEN should succeed`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(emptyList())
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE) } returns Result.success(Unit)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN repository throws WHEN adding morador THEN should fail`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(emptyList())
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE) } returns
            Result.failure(MoradorException.DuplicateMoradorException)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE)

        assertTrue(result.isFailure)
        assertIs<MoradorException.DuplicateMoradorException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN 0 existing proprietarios WHEN adding PROPRIETARIO THEN should succeed`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(emptyList())
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO) } returns Result.success(Unit)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN 1 existing proprietario WHEN adding PROPRIETARIO THEN should succeed`() = runTest {
        val existing = listOf(Morador.dummy.copy(tipo = MoradorTipo.PROPRIETARIO))
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO) } returns Result.success(Unit)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN 2 existing proprietarios WHEN adding PROPRIETARIO THEN should fail with MaxProprietariosExceededException`() = runTest {
        val existing = listOf(
            Morador.dummy.copy(tipo = MoradorTipo.PROPRIETARIO),
            Morador.dummy.copy(tipo = MoradorTipo.PROPRIETARIO)
        )
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isFailure)
        assertIs<MoradorException.MaxProprietariosExceededException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN 2 existing proprietarios WHEN adding RESIDENTE THEN should succeed`() = runTest {
        val existing = listOf(
            Morador.dummy.copy(tipo = MoradorTipo.PROPRIETARIO),
            Morador.dummy.copy(tipo = MoradorTipo.PROPRIETARIO)
        )
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE) } returns Result.success(Unit)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN pessoa already morador in apartamento WHEN adding THEN should fail with DuplicateMoradorException`() = runTest {
        val existingPessoa = Pessoa.dummy.copy(id = "pessoaId")
        val existing = listOf(Morador.dummy.copy(pessoa = existingPessoa, tipo = MoradorTipo.RESIDENTE))
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE)

        assertTrue(result.isFailure)
        assertIs<MoradorException.DuplicateMoradorException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN pessoa already morador in apartamento WHEN adding as PROPRIETARIO THEN should fail with DuplicateMoradorException`() = runTest {
        val existingPessoa = Pessoa.dummy.copy(id = "pessoaId")
        val existing = listOf(Morador.dummy.copy(pessoa = existingPessoa, tipo = MoradorTipo.RESIDENTE))
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)

        val result = addMoradorUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isFailure)
        assertIs<MoradorException.DuplicateMoradorException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN pessoa is morador in apartment A WHEN adding to apartment B THEN should succeed and call addMorador`() = runTest {
        // Pessoa already exists as morador in apartment A
        val pessoaInAptA = Pessoa.dummy.copy(id = "pessoaId")
        val moradorInAptA = Morador.dummy.copy(
            pessoa = pessoaInAptA,
            apartamento = Morador.dummy.apartamento.copy(id = "apartamentoA"),
            tipo = MoradorTipo.RESIDENTE
        )

        // Apartment A has the morador; apartment B is empty
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoA") } returns Result.success(listOf(moradorInAptA))
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoB") } returns Result.success(emptyList())
        everySuspending { moradoresRepository.addMorador("pessoaId", "apartamentoB", MoradorTipo.RESIDENTE) } returns Result.success(Unit)

        // Adding the same pessoa to apartment B should succeed
        val result = addMoradorUseCase("pessoaId", "apartamentoB", MoradorTipo.RESIDENTE)

        assertTrue(result.isSuccess, "Adding pessoa to apartment B should succeed when they are only in apartment A")

        // Verify addMorador was actually called with apartment B args
        verifyWithSuspend(exhaustive = false) {
            moradoresRepository.addMorador("pessoaId", "apartamentoB", MoradorTipo.RESIDENTE)
        }
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
