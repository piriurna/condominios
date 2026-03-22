package com.zalamena.condominios.ui.apartamento

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCaseImpl
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradoresForApartamentoUseCase
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
    private val viewModel by lazy { ApartamentoDetailViewModel(getApartamentoUseCase, getMoradoresForApartamentoUseCase) }

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
}
