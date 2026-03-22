package com.zalamena.condominios.ui.apartamento

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCaseImpl
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.RemoveMoradorUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.RemoveMoradorUseCaseImpl
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailNavEvent
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
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
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ApartamentoDetailViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val getApartamentoUseCase: GetApartamentoUseCase by lazy { GetApartamentoUseCaseImpl(apartamentosRepository) }
    private val getMoradoresForApartamentoUseCase by lazy { GetMoradoresForApartamentoUseCase(moradoresRepository) }
    private val removeMoradorUseCase: RemoveMoradorUseCase by lazy { RemoveMoradorUseCaseImpl(moradoresRepository) }
    private val viewModel by lazy { ApartamentoDetailViewModel(getApartamentoUseCase, getMoradoresForApartamentoUseCase, removeMoradorUseCase) }

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

    @Test
    fun `WHEN onMoradorClick THEN emits MoradorDetail navigation event`() = runTest(testScheduler) {
        viewModel.onMoradorClick("pessoa-123")

        val event = viewModel.uiState.value.navigationEvent
        assertIs<ApartamentoDetailNavEvent.MoradorDetail>(event)
        assertEquals("pessoa-123", event.pessoaId)
    }

    @Test
    fun `GIVEN MoradorDetail event WHEN onNavigationHandled THEN clears event`() = runTest(testScheduler) {
        viewModel.onMoradorClick("pessoa-123")
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `WHEN onAddMoradorClick THEN emits AddMorador navigation event`() = runTest(testScheduler) {
        viewModel.setApartamentoId("apt-1")
        viewModel.onAddMoradorClick()

        val event = viewModel.uiState.value.navigationEvent
        assertIs<ApartamentoDetailNavEvent.AddMorador>(event)
        assertEquals("apt-1", event.apartamentoId)
    }

    @Test
    fun `GIVEN successful load WHEN load THEN moradores have pessoaId`() = runTest(testScheduler) {
        val apt = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList())
        val moradores = listOf(
            Morador(
                pessoa = Pessoa(id = "p-1", cpf = "12345678901", nome = "Joao", email = "", telefone = ""),
                apartamento = apt,
                tipo = MoradorTipo.RESIDENTE
            )
        )
        everySuspending { apartamentosRepository.getApartamento("apt-1") } returns Result.success(apt)
        everySuspending { moradoresRepository.getMoradoresForApartamento("apt-1") } returns Result.success(moradores)

        viewModel.setApartamentoId("apt-1")
        viewModel.load()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isError)
        assertEquals(1, viewModel.uiState.value.moradores.size)
        assertEquals("p-1", viewModel.uiState.value.moradores.first().pessoaId)
    }

    @Test
    fun `GIVEN successful load WHEN load THEN moradores have apartamentoId`() = runTest(testScheduler) {
        val apt = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList())
        val moradores = listOf(
            Morador(
                pessoa = Pessoa(id = "p-1", cpf = "12345678901", nome = "Joao", email = "", telefone = ""),
                apartamento = apt,
                tipo = MoradorTipo.RESIDENTE
            )
        )
        everySuspending { apartamentosRepository.getApartamento("apt-1") } returns Result.success(apt)
        everySuspending { moradoresRepository.getMoradoresForApartamento("apt-1") } returns Result.success(moradores)

        viewModel.setApartamentoId("apt-1")
        viewModel.load()
        advanceUntilIdle()

        assertEquals("apt-1", viewModel.uiState.value.moradores.first().apartamentoId)
    }

    // --- Delete flow ---

    @Test
    fun `WHEN onDeleteMoradorClick THEN showDeleteConfirmation is set`() = runTest(testScheduler) {
        loadMoradores()

        val morador = viewModel.uiState.value.moradores.first()
        viewModel.onDeleteMoradorClick(morador)

        assertNotNull(viewModel.uiState.value.showDeleteConfirmation)
        assertEquals(morador.pessoaId, viewModel.uiState.value.showDeleteConfirmation?.pessoaId)
    }

    @Test
    fun `WHEN onDismissDeleteMorador THEN showDeleteConfirmation is cleared`() = runTest(testScheduler) {
        loadMoradores()

        viewModel.onDeleteMoradorClick(viewModel.uiState.value.moradores.first())
        viewModel.onDismissDeleteMorador()

        assertNull(viewModel.uiState.value.showDeleteConfirmation)
    }

    @Test
    fun `GIVEN delete succeeds WHEN onConfirmDeleteMorador THEN list is refreshed`() = runTest(testScheduler) {
        val apt = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList())
        val moradores = listOf(
            Morador(
                pessoa = Pessoa(id = "p-1", cpf = "12345678901", nome = "Joao", email = "", telefone = ""),
                apartamento = apt,
                tipo = MoradorTipo.RESIDENTE
            )
        )

        everySuspending { apartamentosRepository.getApartamento("apt-1") } returns Result.success(apt)
        var moradoresResponse: Result<List<Morador>> = Result.success(moradores)
        everySuspending { moradoresRepository.getMoradoresForApartamento("apt-1") } runs { moradoresResponse }

        viewModel.setApartamentoId("apt-1")
        viewModel.load()
        advanceUntilIdle()

        val morador = viewModel.uiState.value.moradores.first()
        viewModel.onDeleteMoradorClick(morador)

        everySuspending { moradoresRepository.removeMorador("p-1", "apt-1") } returns Result.success(Unit)
        moradoresResponse = Result.success(emptyList())

        viewModel.onConfirmDeleteMorador()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.showDeleteConfirmation)
        assertTrue(viewModel.uiState.value.moradores.isEmpty())
    }

    // --- Helpers ---

    private suspend fun loadMoradores() {
        val apt = Apartamento(id = "apt-1", numero = "101", andar = "1", moradores = emptyList())
        val moradores = listOf(
            Morador(
                pessoa = Pessoa(id = "p-1", cpf = "12345678901", nome = "Joao", email = "", telefone = ""),
                apartamento = apt,
                tipo = MoradorTipo.RESIDENTE
            )
        )
        everySuspending { apartamentosRepository.getApartamento("apt-1") } returns Result.success(apt)
        everySuspending { moradoresRepository.getMoradoresForApartamento("apt-1") } returns Result.success(moradores)

        viewModel.setApartamentoId("apt-1")
        viewModel.load()
        testScheduler.advanceUntilIdle()
    }
}
