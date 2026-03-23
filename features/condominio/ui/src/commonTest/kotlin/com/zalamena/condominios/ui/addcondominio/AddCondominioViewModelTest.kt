package com.zalamena.condominios.ui.addcondominio

import com.zalamena.condominios.condominio.domain.address.CepLookupProvider
import com.zalamena.condominios.condominio.domain.address.CepResult
import com.zalamena.condominios.condominio.domain.address.Cidade
import com.zalamena.condominios.condominio.domain.address.CidadeProvider
import com.zalamena.condominios.condominio.domain.address.Estado
import com.zalamena.condominios.condominio.domain.address.EstadoProvider
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.AddCondominioUseCase
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddCondominioViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val sampleEstados = listOf(
        Estado(id = 35, sigla = "SP", nome = "Sao Paulo"),
        Estado(id = 33, sigla = "RJ", nome = "Rio de Janeiro")
    )

    private val sampleCidades = listOf(
        Cidade(id = 3550308, nome = "Sao Paulo"),
        Cidade(id = 3509502, nome = "Campinas")
    )

    private val successEstadoProvider = EstadoProvider { Result.success(sampleEstados) }
    private val failEstadoProvider = EstadoProvider { Result.failure(Exception("Network error")) }
    private val successCidadeProvider = CidadeProvider { Result.success(sampleCidades) }
    // A no-op provider that just succeeds without changing anything meaningful
    private val noCepLookupProvider = CepLookupProvider {
        Result.success(CepResult(rua = "", cidade = "", estado = ""))
    }
    private val successCepLookupProvider = CepLookupProvider {
        Result.success(CepResult(rua = "Praca da Se", cidade = "Sao Paulo", estado = "SP"))
    }
    private val failCepLookupProvider = CepLookupProvider {
        Result.failure(IllegalStateException("CEP nao encontrado"))
    }

    private val addCondominioUseCase by lazy { AddCondominioUseCase(condominioRepository) }

    private fun createViewModel(
        estadoProvider: EstadoProvider = successEstadoProvider,
        cidadeProvider: CidadeProvider = successCidadeProvider,
        cepLookupProvider: CepLookupProvider = noCepLookupProvider
    ) = AddCondominioViewModel(addCondominioUseCase, estadoProvider, cidadeProvider, cepLookupProvider)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Initial state ---

    @Test
    fun `GIVEN initial state WHEN observing THEN isLoading is false`() = runTest {
        val viewModel = createViewModel()
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no error`() = runTest {
        val viewModel = createViewModel()
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN no createdCondominioId`() = runTest {
        val viewModel = createViewModel()
        assertNull(viewModel.uiState.value.createdCondominioId)
    }

    @Test
    fun `GIVEN initial state WHEN observing THEN form is blank`() = runTest {
        val viewModel = createViewModel()
        val form = viewModel.uiState.value.form
        assertTrue(form.nome.isBlank())
        assertTrue(form.rua.isBlank())
        assertTrue(form.cep.isBlank())
    }

    // --- Form setters ---

    @Test
    fun `WHEN setting nome THEN form nome is updated`() = runTest {
        val viewModel = createViewModel()
        viewModel.setNome("Edificio Central")
        assertEquals("Edificio Central", viewModel.uiState.value.form.nome)
    }

    @Test
    fun `WHEN setting rua THEN form rua is updated`() = runTest {
        val viewModel = createViewModel()
        viewModel.setRua("Rua das Flores")
        assertEquals("Rua das Flores", viewModel.uiState.value.form.rua)
    }

    @Test
    fun `WHEN setting cep THEN form cep is updated`() = runTest {
        val viewModel = createViewModel()
        viewModel.setCep("12345-678")
        assertEquals("12345-678", viewModel.uiState.value.form.cep)
    }

    // --- Estados loading on init ---

    @Test
    fun `GIVEN successful estado provider WHEN init THEN estados are loaded`() = runTest {
        val viewModel = createViewModel(estadoProvider = successEstadoProvider)

        assertEquals(2, viewModel.uiState.value.estados.size)
        assertEquals("SP", viewModel.uiState.value.estados[0].sigla)
        assertFalse(viewModel.uiState.value.isLoadingEstados)
        assertTrue(viewModel.uiState.value.useDropdowns)
    }

    @Test
    fun `GIVEN failing estado provider WHEN init THEN useDropdowns is false`() = runTest {
        val viewModel = createViewModel(estadoProvider = failEstadoProvider)

        assertTrue(viewModel.uiState.value.estados.isEmpty())
        assertFalse(viewModel.uiState.value.isLoadingEstados)
        assertFalse(viewModel.uiState.value.useDropdowns)
    }

    // --- Selecting estado triggers cidade loading ---

    @Test
    fun `GIVEN useDropdowns true WHEN setEstado THEN cidades are loaded`() = runTest {
        val viewModel = createViewModel()

        viewModel.setEstado("SP")

        assertEquals("SP", viewModel.uiState.value.form.estado)
        assertEquals(2, viewModel.uiState.value.cidades.size)
        assertFalse(viewModel.uiState.value.isLoadingCidades)
    }

    @Test
    fun `WHEN setEstado THEN cidade field is cleared`() = runTest {
        val viewModel = createViewModel()
        viewModel.setCidade("Campinas")
        viewModel.setEstado("RJ")

        assertEquals("", viewModel.uiState.value.form.cidade)
    }

    // --- CEP auto-fill ---

    @Test
    fun `GIVEN 8-digit CEP WHEN setCep THEN triggers lookup and auto-fills fields`() = runTest {
        val viewModel = createViewModel(cepLookupProvider = successCepLookupProvider)

        viewModel.setCep("01001000")

        assertEquals("Praca da Se", viewModel.uiState.value.form.rua)
        assertEquals("Sao Paulo", viewModel.uiState.value.form.cidade)
        assertEquals("SP", viewModel.uiState.value.form.estado)
        assertFalse(viewModel.uiState.value.isLoadingCep)
        assertNull(viewModel.uiState.value.cepMessage)
    }

    @Test
    fun `GIVEN 8-digit CEP with mask WHEN setCep THEN triggers lookup`() = runTest {
        val viewModel = createViewModel(cepLookupProvider = successCepLookupProvider)

        viewModel.setCep("01001-000")

        assertEquals("Praca da Se", viewModel.uiState.value.form.rua)
    }

    @Test
    fun `GIVEN less than 8 digits WHEN setCep THEN does not trigger lookup`() = runTest {
        val viewModel = createViewModel(cepLookupProvider = failCepLookupProvider)

        viewModel.setCep("0100100") // 7 digits

        assertEquals("0100100", viewModel.uiState.value.form.cep)
        assertNull(viewModel.uiState.value.cepMessage) // no lookup happened
    }

    @Test
    fun `GIVEN CEP lookup fails WHEN setCep THEN shows cepMessage`() = runTest {
        val viewModel = createViewModel(cepLookupProvider = failCepLookupProvider)

        viewModel.setCep("00000000")

        assertEquals("CEP nao encontrado", viewModel.uiState.value.cepMessage)
        assertFalse(viewModel.uiState.value.isLoadingCep)
    }

    // --- addCondominio ---

    @Test
    fun `GIVEN valid form WHEN adding THEN sets createdCondominioId on success`() = runTest {
        val viewModel = createViewModel()
        fillValidForm(viewModel)
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.success(Unit)

        viewModel.addCondominio()

        assertNotNull(viewModel.uiState.value.createdCondominioId)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `GIVEN blank form WHEN adding THEN sets per-field errors`() = runTest {
        val viewModel = createViewModel()
        viewModel.addCondominio()

        assertNotNull(viewModel.uiState.value.nomeError)
        assertNotNull(viewModel.uiState.value.ruaError)
        assertNotNull(viewModel.uiState.value.numeroError)
        assertNotNull(viewModel.uiState.value.cepError)
        assertNotNull(viewModel.uiState.value.cidadeError)
        assertNotNull(viewModel.uiState.value.estadoError)
        assertNull(viewModel.uiState.value.createdCondominioId)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN only nome blank WHEN adding THEN only nomeError is set`() = runTest {
        val viewModel = createViewModel()
        viewModel.setNumero("123")
        viewModel.setCep("12345678")
        // Set address fields after setCep since lookup may overwrite them
        viewModel.setRua("Rua das Flores")
        viewModel.setEstado("SP")
        viewModel.setCidade("Sao Paulo")

        viewModel.addCondominio()

        assertNotNull(viewModel.uiState.value.nomeError)
        assertNull(viewModel.uiState.value.ruaError)
        assertNull(viewModel.uiState.value.numeroError)
        assertNull(viewModel.uiState.value.cepError)
        assertNull(viewModel.uiState.value.cidadeError)
        assertNull(viewModel.uiState.value.estadoError)
    }

    @Test
    fun `GIVEN invalid CEP WHEN adding THEN cepError shows digit count message`() = runTest {
        val viewModel = createViewModel()
        viewModel.setNome("Condominio Teste")
        viewModel.setRua("Rua das Flores")
        viewModel.setNumero("123")
        viewModel.setCep("123")
        viewModel.setCidade("Sao Paulo")
        viewModel.setEstado("SP")

        viewModel.addCondominio()

        assertEquals("CEP deve ter 8 digitos", viewModel.uiState.value.cepError)
        assertNull(viewModel.uiState.value.nomeError)
    }

    @Test
    fun `GIVEN nomeError exists WHEN setNome THEN nomeError is cleared`() = runTest {
        val viewModel = createViewModel()
        viewModel.addCondominio() // triggers errors
        assertNotNull(viewModel.uiState.value.nomeError)

        viewModel.setNome("Nome")
        assertNull(viewModel.uiState.value.nomeError)
    }

    @Test
    fun `GIVEN ruaError exists WHEN setRua THEN ruaError is cleared`() = runTest {
        val viewModel = createViewModel()
        viewModel.addCondominio()
        assertNotNull(viewModel.uiState.value.ruaError)

        viewModel.setRua("Rua")
        assertNull(viewModel.uiState.value.ruaError)
    }

    @Test
    fun `GIVEN cepError exists WHEN setCep THEN cepError is cleared`() = runTest {
        val viewModel = createViewModel()
        viewModel.addCondominio()
        assertNotNull(viewModel.uiState.value.cepError)

        viewModel.setCep("12345678")
        assertNull(viewModel.uiState.value.cepError)
    }

    @Test
    fun `GIVEN valid form WHEN repo fails THEN sets error message`() = runTest {
        val viewModel = createViewModel()
        fillValidForm(viewModel)
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.failure(Exception("DB error"))

        viewModel.addCondominio()

        assertEquals("DB error", viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.createdCondominioId)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN error set WHEN changing form field THEN error is cleared`() = runTest {
        val viewModel = createViewModel()
        fillValidForm(viewModel)
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.failure(Exception("DB error"))
        viewModel.addCondominio()
        assertEquals("DB error", viewModel.uiState.value.errorMessage)

        viewModel.setNome("Nome")

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // --- reset ---

    @Test
    fun `GIVEN form filled and condominio created WHEN reset THEN state is cleared`() = runTest {
        val viewModel = createViewModel()
        fillValidForm(viewModel)
        everySuspending { condominioRepository.getCondominios() } returns Result.success(emptyList())
        everySuspending { condominioRepository.addCondominio(isAny()) } returns Result.success(Unit)
        viewModel.addCondominio()

        viewModel.reset()

        val state = viewModel.uiState.value
        assertNull(state.createdCondominioId)
        assertNull(state.errorMessage)
        assertFalse(state.isLoading)
        assertTrue(state.form.nome.isBlank())
        assertTrue(state.form.rua.isBlank())
    }

    // --- Helpers ---

    private fun fillValidForm(viewModel: AddCondominioViewModel) {
        viewModel.setNome("Condominio Teste")
        viewModel.setNumero("123")
        viewModel.setCep("12345-678")
        // Set address fields AFTER setCep, since CEP lookup may overwrite them
        viewModel.setRua("Rua das Flores")
        viewModel.setEstado("SP")
        viewModel.setCidade("Sao Paulo")
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
