package com.zalamena.condominios.ui.addmorador

import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCase
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.condominio.ui.moradores.mapper.toSelectUi
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

    @Mock
    lateinit var addMoradorUseCase: AddMoradorUseCase

    private val viewModel by lazy {
        AddMoradorOverviewViewModel(
            getPessoaUseCase,
            getApartamentoUseCase,
            addMoradorUseCase
        )
    }


    @BeforeTest
    fun setup() {
        val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
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
    fun `GIVEN valid pessoa and apartamento id WHEN populating form THEN populate pessoa and apartamento`() = runTest {
        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.success(Pessoa.dummy)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.success(Apartamento.dummy)

        viewModel.populateForm("pessoaId", "apartamentoId")

        assertEquals(Pessoa.dummy.toSelectUi(), viewModel.uiState.value.pessoa)
        assertEquals(Apartamento.dummy.toSelectUi(), viewModel.uiState.value.apartamento)
    }

    @Test
    fun `GIVEN null apartamento id WHEN populating form THEN fail populating the form`() = runTest {
        viewModel.populateForm("pessoaId", null)

        assertEquals("Pessoa or Apartamento not found", viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN get pessoa and get apartamento fails WHEN populating form THEN fail populating the form`() = runTest {
        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.failure(PessoaException.PessoaNotFoundException)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.failure(ApartamentoException.NoApartmentFoundException)


        viewModel.populateForm("pessoaId", "apartamentoId")

        assertEquals(PessoaException.PessoaNotFoundException.message, viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN get pessoa fails and get apartamento succeeds WHEN populating form THEN fail populating the form`() = runTest {
        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.failure(PessoaException.PessoaNotFoundException)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.success(Apartamento.dummy)


        viewModel.populateForm("pessoaId", "apartamentoId")

        assertEquals(PessoaException.PessoaNotFoundException.message, viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }

    @Test
    fun `GIVEN get pessoa succeeds and get apartamento fails WHEN populating form THEN fail populating the form`() = runTest {
        everySuspending { getPessoaUseCase("pessoaId") } returns
                Result.success(Pessoa.dummy)
        everySuspending { getApartamentoUseCase("apartamentoId") } returns
                Result.failure(ApartamentoException.NoApartmentFoundException)


        viewModel.populateForm("pessoaId", "apartamentoId")

        assertEquals(ApartamentoException.NoApartmentFoundException.message, viewModel.uiState.value.error)
        assertEquals(null, viewModel.uiState.value.apartamento)
        assertEquals(null, viewModel.uiState.value.pessoa)
    }


    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}