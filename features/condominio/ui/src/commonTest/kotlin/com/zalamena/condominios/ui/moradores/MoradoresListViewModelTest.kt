package com.zalamena.condominios.ui.moradores

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.ui.moradores.list.MoradoresListViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MoradoresListViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var condominioRepository: CondominioRepository

    private val getMoradoresUseCase by lazy { GetMoradoresUseCase(condominioRepository) }
    private val viewModel by lazy { MoradoresListViewModel(getMoradoresUseCase) }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN moradores should be empty`() = runTest {
        assertEquals(emptyList(), viewModel.uiState.first().moradores)
    }

    @Test
    fun `GIVEN moradores exist WHEN getting moradores THEN should populate list`() = runTest {
        everySuspending { condominioRepository.getCondominio("condominioId") } returns Result.success(Condominio.dummy)

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertTrue(state.moradores.isNotEmpty())
    }

    @Test
    fun `GIVEN no moradores WHEN getting moradores THEN should return empty list`() = runTest {
        val condominioNoMoradores = Condominio.dummy.copy(
            apartamentos = Condominio.dummy.apartamentos.map { it.copy(moradores = emptyList()) }
        )
        everySuspending { condominioRepository.getCondominio("condominioId") } returns Result.success(condominioNoMoradores)

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.moradores)
    }

    @Test
    fun `GIVEN fetch fails WHEN getting moradores THEN should return empty list`() = runTest {
        everySuspending { condominioRepository.getCondominio("condominioId") } returns Result.failure(Exception("Not found"))

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.moradores)
    }

    @Test
    fun `GIVEN condominio with no apartamentos WHEN getting moradores THEN should return empty list`() = runTest {
        val condominioNoApartamentos = Condominio.dummy.copy(apartamentos = emptyList())
        everySuspending { condominioRepository.getCondominio("condominioId") } returns Result.success(condominioNoApartamentos)

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.moradores)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
