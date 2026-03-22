package com.zalamena.condominios.ui.moradores

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorDetailUseCase
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MoradorInfoViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getMoradorDetailUseCase by lazy { GetMoradorDetailUseCase(moradoresRepository) }
    private val viewModel by lazy { MoradorInfoViewModel(getMoradorDetailUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    // --- Initial state ---

    @Test
    fun `GIVEN initial state WHEN observing THEN nome is empty`() = runTest(testScheduler) {
        assertEquals("", viewModel.uiState.value.nome)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isError)
    }

    // --- Successful load ---

    @Test
    fun `GIVEN moradores exist WHEN load THEN populates pessoa info from first entry`() = runTest(testScheduler) {
        val pessoa = Pessoa(id = "p1", cpf = "12345678901", nome = "Joao Silva", email = "joao@test.com", telefone = "11999999999")
        val moradores = listOf(
            Morador(
                pessoa = pessoa,
                apartamento = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList()),
                tipo = MoradorTipo.PROPRIETARIO
            )
        )
        everySuspending { moradoresRepository.getMoradoresForPessoa("p1") } returns Result.success(moradores)

        viewModel.setPessoaId("p1")
        viewModel.load()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals("Joao Silva", state.nome)
        assertEquals("***.***.***-01", state.maskedCpf)
        assertEquals("joao@test.com", state.email)
        assertEquals("11999999999", state.telefone)
    }

    @Test
    fun `GIVEN multiple apartments WHEN load THEN lists all apartments`() = runTest(testScheduler) {
        val pessoa = Pessoa(id = "p1", cpf = "12345678901", nome = "Joao", email = "j@t.com", telefone = "119")
        val moradores = listOf(
            Morador(pessoa = pessoa, apartamento = Apartamento("apt-1", "101", "1", emptyList()), tipo = MoradorTipo.PROPRIETARIO),
            Morador(pessoa = pessoa, apartamento = Apartamento("apt-2", "202", "2", emptyList()), tipo = MoradorTipo.RESIDENTE)
        )
        everySuspending { moradoresRepository.getMoradoresForPessoa("p1") } returns Result.success(moradores)

        viewModel.setPessoaId("p1")
        viewModel.load()
        advanceUntilIdle()

        val apts = viewModel.uiState.value.apartamentos
        assertEquals(2, apts.size)
        assertEquals("101", apts[0].numero)
        assertEquals("202", apts[1].numero)
    }

    // --- Missing info ---

    @Test
    fun `GIVEN blank email WHEN load THEN shows Nao informado`() = runTest(testScheduler) {
        val pessoa = Pessoa(id = "p1", cpf = "12345678901", nome = "Maria", email = "", telefone = "119")
        val moradores = listOf(
            Morador(pessoa = pessoa, apartamento = Apartamento("apt-1", "101", "1", emptyList()), tipo = MoradorTipo.RESIDENTE)
        )
        everySuspending { moradoresRepository.getMoradoresForPessoa("p1") } returns Result.success(moradores)

        viewModel.setPessoaId("p1")
        viewModel.load()
        advanceUntilIdle()

        assertEquals("Nao informado", viewModel.uiState.value.email)
    }

    @Test
    fun `GIVEN blank telefone WHEN load THEN shows Nao informado`() = runTest(testScheduler) {
        val pessoa = Pessoa(id = "p1", cpf = "12345678901", nome = "Maria", email = "m@t.com", telefone = "")
        val moradores = listOf(
            Morador(pessoa = pessoa, apartamento = Apartamento("apt-1", "101", "1", emptyList()), tipo = MoradorTipo.RESIDENTE)
        )
        everySuspending { moradoresRepository.getMoradoresForPessoa("p1") } returns Result.success(moradores)

        viewModel.setPessoaId("p1")
        viewModel.load()
        advanceUntilIdle()

        assertEquals("Nao informado", viewModel.uiState.value.telefone)
    }

    // --- Error state ---

    @Test
    fun `GIVEN repository fails WHEN load THEN sets error state`() = runTest(testScheduler) {
        everySuspending { moradoresRepository.getMoradoresForPessoa("p1") } returns Result.failure(Exception("DB error"))

        viewModel.setPessoaId("p1")
        viewModel.load()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN empty moradores list WHEN load THEN sets error state`() = runTest(testScheduler) {
        everySuspending { moradoresRepository.getMoradoresForPessoa("p1") } returns Result.success(emptyList())

        viewModel.setPessoaId("p1")
        viewModel.load()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isError)
    }

    @Test
    fun `GIVEN blank pessoaId WHEN load THEN does nothing`() = runTest(testScheduler) {
        viewModel.load()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("", viewModel.uiState.value.nome)
    }
}
