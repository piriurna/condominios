package com.zalamena.condominios.ui.moradores

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentosUseCase
import com.zalamena.condominios.condominio.ui.moradores.add.AddMoradorViewModel
import com.zalamena.condominios.condominio.ui.moradores.mapper.toSelectUi
import com.zalamena.condominios.condominio.ui.moradores.models.SelectApartamentoUiData
import com.zalamena.condominios.condominio.ui.moradores.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoasListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddMoradorViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var pessoaRepository: PessoaRepository

    @Mock
    lateinit var apartamentosRepository: ApartamentosRepository

    private val getPessoasListUseCase by lazy { GetPessoasListUseCase(pessoaRepository) }
    private val getApartamentosUseCase by lazy { GetApartamentosUseCase(apartamentosRepository) }
    private val viewModel by lazy { AddMoradorViewModel(getPessoasListUseCase, getApartamentosUseCase) }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN lists should be empty`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(emptyList(), state.pessoasList)
        assertEquals(emptyList(), state.apartamentosList)
        assertNull(state.pessoaSelected)
        assertNull(state.apartamentoSelected)
    }

    @Test
    fun `GIVEN data available WHEN populating form THEN should populate lists`() = runTest {
        everySuspending { pessoaRepository.getAllIndividos() } returns Result.success(listOf(Pessoa.dummy))
        everySuspending { apartamentosRepository.getApartamentos() } returns Result.success(listOf(Apartamento.dummy))

        viewModel.populateForm()

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(listOf(Pessoa.dummy.toSelectUi()), state.pessoasList)
        assertEquals(listOf(Apartamento.dummy.toSelectUi()), state.apartamentosList)
    }

    @Test
    fun `GIVEN data fetch fails WHEN populating form THEN lists should be empty`() = runTest {
        everySuspending { pessoaRepository.getAllIndividos() } returns Result.failure(Exception("Error"))
        everySuspending { apartamentosRepository.getApartamentos() } returns Result.failure(Exception("Error"))

        viewModel.populateForm()

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.pessoasList)
        assertEquals(emptyList(), state.apartamentosList)
    }

    @Test
    fun `GIVEN pessoa selected WHEN selecting pessoa THEN should update selected pessoa`() = runTest {
        val pessoa = SelectPessoaUiData(id = "id", nome = "nome", cpf = "cpf")
        viewModel.pessoaSelected(pessoa)

        assertEquals(pessoa, viewModel.uiState.first().pessoaSelected)
    }

    @Test
    fun `GIVEN apartamento selected WHEN selecting apartamento THEN should update selected apartamento`() = runTest {
        val apartamento = SelectApartamentoUiData(id = "id", numero = "101")
        viewModel.apartamentoSelected(apartamento)

        assertEquals(apartamento, viewModel.uiState.first().apartamentoSelected)
    }

    @Test
    fun `GIVEN no pessoa or apartamento selected WHEN adding morador THEN should not crash`() = runTest {
        viewModel.addMorador()

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
