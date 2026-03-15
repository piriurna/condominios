package com.zalamena.condominios.ui.moradores

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorUseCase
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.condominio.ui.moradores.mapper.toUi
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoradorInfoViewModelTest : TestsWithMocks() {

    @Mock
    lateinit var moradoresRepository: MoradoresRepository

    private val getMoradorUseCase by lazy { GetMoradorUseCase(moradoresRepository) }
    private val viewModel by lazy { MoradorInfoViewModel(getMoradorUseCase) }

    @Test
    fun `GIVEN initial state WHEN observing uiState THEN morador should be null`() = runTest {
        assertNull(viewModel.uiState.value.morador)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN morador exists WHEN getting morador THEN should populate morador`() = runTest {
        everySuspending { moradoresRepository.getMorador("cpf", "apartamentoId") } returns Result.success(Morador.dummy)

        viewModel.getMorador("cpf", "apartamentoId")

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(Morador.dummy.toUi(), viewModel.uiState.value.morador)
    }

    @Test
    fun `GIVEN morador does not exist WHEN getting morador THEN morador should be null`() = runTest {
        everySuspending { moradoresRepository.getMorador("cpf", "apartamentoId") } returns Result.failure(MoradorException.MoradorNotFoundException)

        viewModel.getMorador("cpf", "apartamentoId")

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.morador)
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
