package com.zalamena.condominios.ui.moradores

import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
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
    lateinit var moradoresRepository: MoradoresRepository

    private val getMoradoresUseCase by lazy { GetMoradoresUseCase(moradoresRepository) }
    private val viewModel by lazy { MoradoresListViewModel(getMoradoresUseCase) }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN moradores should be empty`() = runTest {
        assertEquals(emptyList(), viewModel.uiState.first().moradores)
    }

    @Test
    fun `GIVEN moradores exist WHEN getting moradores THEN should populate list`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condominioId") } returns Result.success(listOf(Morador.dummy))

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertTrue(state.moradores.isNotEmpty())
    }

    @Test
    fun `GIVEN no moradores WHEN getting moradores THEN should return empty list`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condominioId") } returns Result.success(emptyList())

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.moradores)
    }

    @Test
    fun `GIVEN fetch fails WHEN getting moradores THEN should return empty list`() = runTest {
        everySuspending { moradoresRepository.getMoradoresForCondominio("condominioId") } returns Result.failure(Exception("Not found"))

        viewModel.getMoradores("condominioId")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.moradores)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
