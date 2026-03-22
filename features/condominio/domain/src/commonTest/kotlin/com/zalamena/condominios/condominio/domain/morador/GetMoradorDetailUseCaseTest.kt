package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorDetailUseCase
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMoradorDetailUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val useCase by lazy { GetMoradorDetailUseCase(moradoresRepository) }

    @Test
    fun `GIVEN pessoa with moradores WHEN invoking THEN returns list of moradores`() = runTest {
        val pessoa = Pessoa.dummy.copy(id = "pessoa-1")
        val moradores = listOf(
            Morador(
                pessoa = pessoa,
                apartamento = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList()),
                tipo = MoradorTipo.PROPRIETARIO
            ),
            Morador(
                pessoa = pessoa,
                apartamento = Apartamento(id = "apt-2", numero = "202", andar = "2", moradores = emptyList()),
                tipo = MoradorTipo.RESIDENTE
            )
        )

        everySuspending { moradoresRepository.getMoradoresForPessoa("pessoa-1") } returns Result.success(moradores)

        val result = useCase("pessoa-1")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
        assertEquals(MoradorTipo.PROPRIETARIO, result.getOrThrow()[0].tipo)
        assertEquals(MoradorTipo.RESIDENTE, result.getOrThrow()[1].tipo)
    }

    @Test
    fun `GIVEN no moradores WHEN invoking THEN returns empty list`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForPessoa("pessoa-1") } returns Result.success(emptyList())

        val result = useCase("pessoa-1")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN repository fails WHEN invoking THEN propagates failure`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForPessoa("pessoa-1") } returns Result.failure(Exception("DB error"))

        val result = useCase("pessoa-1")

        assertTrue(result.isFailure)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
