package com.zalamena.condominios.condominio.domain.morador

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.repository.PessoaProvider
import com.zalamena.condominios.condominio.domain.morador.usecase.GetAvailablePessoasForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAvailablePessoasForApartamentoUseCaseTest : TestsWithMocks() {

    @Mock
    lateinit var pessoaProvider: PessoaProvider

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val getMoradoresForApartamentoUseCase by lazy { GetMoradoresForApartamentoUseCase(moradoresRepository) }
    private val useCase by lazy { GetAvailablePessoasForApartamentoUseCase(pessoaProvider, getMoradoresForApartamentoUseCase) }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Test
    fun `GIVEN apartamentoId is null WHEN invoked THEN returns all pessoas`() = runTest {
        val pessoas = listOf(makePessoa("Joao"), makePessoa("Maria"))
        everySuspending { pessoaProvider.getAllPessoas() } returns Result.success(pessoas)

        val result = useCase(null)

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN apartamentoId provided WHEN invoked THEN filters out existing moradores`() = runTest {
        val joao = makePessoa("Joao")
        val maria = makePessoa("Maria")
        val pedro = makePessoa("Pedro")

        val existingMorador = Morador(
            pessoa = joao,
            apartamento = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList()),
            tipo = MoradorTipo.RESIDENTE
        )

        everySuspending { pessoaProvider.getAllPessoas() } returns Result.success(listOf(joao, maria, pedro))
        everySuspending { moradoresRepository.getMoradoresForApartamento("apt-1") } returns Result.success(listOf(existingMorador))

        val result = useCase("apt-1")

        assertTrue(result.isSuccess)
        val available = result.getOrThrow()
        assertEquals(2, available.size)
        assertTrue(available.none { it.id == joao.id })
        assertTrue(available.any { it.id == maria.id })
        assertTrue(available.any { it.id == pedro.id })
    }

    @Test
    fun `GIVEN pessoas fetch fails WHEN invoked THEN returns failure`() = runTest {
        val exception = Exception("DB error")
        everySuspending { pessoaProvider.getAllPessoas() } returns Result.failure(exception)

        val result = useCase("apt-1")

        assertTrue(result.isFailure)
        assertEquals("DB error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `GIVEN moradores fetch fails WHEN invoked THEN returns all pessoas gracefully`() = runTest {
        val joao = makePessoa("Joao")
        val maria = makePessoa("Maria")

        everySuspending { pessoaProvider.getAllPessoas() } returns Result.success(listOf(joao, maria))
        everySuspending { moradoresRepository.getMoradoresForApartamento("apt-1") } returns Result.failure(Exception("error"))

        val result = useCase("apt-1")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    private fun makePessoa(nome: String): Pessoa = Pessoa(
        id = "pessoa-$nome",
        cpf = "12345678900",
        nome = nome,
        email = "$nome@test.com",
        telefone = "11999999999"
    )
}
