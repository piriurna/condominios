package com.zalamena.condominios.addmorador.ui

import androidx.lifecycle.SavedStateHandle
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.apartamentos.domain.usecase.GetApartamentoUseCase
import com.zalamena.condominios.moradores.ui.mapper.toSelectUi
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
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

@OptIn(ExperimentalCoroutinesApi::class)
class AddMoradorOverviewViewModelTest: TestsWithMocks() {

    @Mock
    lateinit var getPessoaUseCase: GetPessoaUseCase

    @Mock
    lateinit var getApartamentoUseCase: GetApartamentoUseCase

    private var savedStateHandle: SavedStateHandle = SavedStateHandle()

    private lateinit var viewModel: AddMoradorOverviewViewModel


    @BeforeTest
    fun setup() {
        val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        reInitViewModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN entering screen WHEN getting uiState THEN pessoa should be null`() = runTest {
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN entering screen WHEN getting uiState THEN apartamento should be null`() = runTest {
        assertEquals(null, viewModel.uiState.value.apartamento)
    }

    @Test
    fun `GIVEN saved state handle valid WHEN populating form THEN populate pessoa and apartamento`() = runTest {
        reInitViewModel(
            SavedStateHandle(
                initialState = mapOf("pessoaId" to "pessoaId","apartamentoId" to "apartamentoId")
            )
        )
        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.success(Pessoa.dummy)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.success(Apartamento.dummy)

        viewModel.populateForm()

        assertEquals(Pessoa.dummy.toSelectUi(), viewModel.uiState.value.pessoa)
        assertEquals(Apartamento.dummy.toSelectUi(), viewModel.uiState.value.apartamento)
    }

    @Test
    fun `GIVEN saved state handle invalid WHEN populating form THEN fail populating the form`() = runTest {
        reInitViewModel(
            SavedStateHandle(
                initialState = mapOf("pessoaId" to "pessoaId")
            )
        )

        viewModel.populateForm()

        assertEquals("Pessoa or Apartamento not found", viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN get pessoa and get apartamento fails WHEN populating form THEN fail populating the form`() = runTest {
        reInitViewModel(
            SavedStateHandle(
                initialState = mapOf("pessoaId" to "pessoaId", "apartamentoId" to "apartamentoId")
            )
        )

        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.failure(PessoaException.PessoaNotFoundException)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.failure(ApartamentoException.NoApartmentFoundException)


        viewModel.populateForm()

        assertEquals("Pessoa or Apartamento not found", viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN get pessoa fails and get apartamento succeeds WHEN populating form THEN fail populating the form`() = runTest {
        reInitViewModel(
            SavedStateHandle(
                initialState = mapOf("pessoaId" to "pessoaId", "apartamentoId" to "apartamentoId")
            )
        )

        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.failure(PessoaException.PessoaNotFoundException)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.success(Apartamento.dummy)


        viewModel.populateForm()

        assertEquals("Pessoa or Apartamento not found", viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN get pessoa succeeds and get apartamento fails WHEN populating form THEN fail populating the form`() = runTest {
        reInitViewModel(
            SavedStateHandle(
                initialState = mapOf("pessoaId" to "pessoaId", "apartamentoId" to "apartamentoId")
            )
        )

        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.success(Pessoa.dummy)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.failure(ApartamentoException.NoApartmentFoundException)


        viewModel.populateForm()

        assertEquals("Pessoa or Apartamento not found", viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    private fun reInitViewModel(savedStateHandle: SavedStateHandle = this.savedStateHandle) {
        viewModel = AddMoradorOverviewViewModel(getPessoaUseCase, getApartamentoUseCase, savedStateHandle)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}