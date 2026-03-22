package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.UpdateMoradorTipoUseCaseImpl
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpdateMoradorTipoUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val updateMoradorTipoUseCase by lazy { UpdateMoradorTipoUseCaseImpl(moradoresRepository) }

    @Test
    fun `GIVEN changing to RESIDENTE WHEN updating THEN should succeed without checking proprietario count`() = runTest {
        everySuspending { moradoresRepository.updateMoradorTipo("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE) } returns Result.success(Unit)

        val result = updateMoradorTipoUseCase("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN 0 existing proprietarios WHEN changing to PROPRIETARIO THEN should succeed`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(emptyList())
        everySuspending { moradoresRepository.updateMoradorTipo("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO) } returns Result.success(Unit)

        val result = updateMoradorTipoUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN 1 existing proprietario (other person) WHEN changing to PROPRIETARIO THEN should succeed`() = runTest {
        val existing = listOf(
            Morador.dummy.copy(
                pessoa = Pessoa.dummy.copy(id = "other-pessoa"),
                tipo = MoradorTipo.PROPRIETARIO
            )
        )
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)
        everySuspending { moradoresRepository.updateMoradorTipo("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO) } returns Result.success(Unit)

        val result = updateMoradorTipoUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN 2 existing proprietarios (other people) WHEN changing to PROPRIETARIO THEN should fail`() = runTest {
        val existing = listOf(
            Morador.dummy.copy(pessoa = Pessoa.dummy.copy(id = "other-1"), tipo = MoradorTipo.PROPRIETARIO),
            Morador.dummy.copy(pessoa = Pessoa.dummy.copy(id = "other-2"), tipo = MoradorTipo.PROPRIETARIO)
        )
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)

        val result = updateMoradorTipoUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isFailure)
        assertIs<MoradorException.MaxProprietariosExceededException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN morador is already PROPRIETARIO and 1 other PROPRIETARIO exists WHEN re-saving as PROPRIETARIO THEN should succeed`() = runTest {
        val existing = listOf(
            Morador.dummy.copy(pessoa = Pessoa.dummy.copy(id = "pessoaId"), tipo = MoradorTipo.PROPRIETARIO),
            Morador.dummy.copy(pessoa = Pessoa.dummy.copy(id = "other-1"), tipo = MoradorTipo.PROPRIETARIO)
        )
        everySuspending { moradoresRepository.getMoradoresForApartamento("apartamentoId") } returns Result.success(existing)
        everySuspending { moradoresRepository.updateMoradorTipo("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO) } returns Result.success(Unit)

        val result = updateMoradorTipoUseCase("pessoaId", "apartamentoId", MoradorTipo.PROPRIETARIO)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN repository fails WHEN updating THEN should propagate failure`() = runTest {
        everySuspending { moradoresRepository.updateMoradorTipo("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE) } returns Result.failure(Exception("DB error"))

        val result = updateMoradorTipoUseCase("pessoaId", "apartamentoId", MoradorTipo.RESIDENTE)

        assertTrue(result.isFailure)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
