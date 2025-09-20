package com.zalamena.condominios.addmorador.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.apartamentos.domain.usecase.GetApartamentoUseCase
import com.zalamena.condominios.moradores.ui.mapper.toSelectUi
import com.zalamena.condominios.moradores.ui.models.SelectApartamentoUiData
import com.zalamena.condominios.moradores.ui.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddMoradorOverviewUiState(
    val isLoading: Boolean = false,
    val pessoa: SelectPessoaUiData? = null,
    val apartamento: SelectApartamentoUiData? = null,
    val error: String? = null
)

class AddMoradorOverviewViewModel(
    private val getPessoaUseCase: GetPessoaUseCase,
    private val getApartamentoUseCase: GetApartamentoUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState: MutableStateFlow<AddMoradorOverviewUiState> = MutableStateFlow(AddMoradorOverviewUiState())
    val uiState: StateFlow<AddMoradorOverviewUiState> = _uiState.asStateFlow()


    fun populateForm() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            val selectedPessoaId: String? = savedStateHandle["pessoaId"]
            val selectedApartamentoId: String? = savedStateHandle["apartamentoId"]

            if(selectedPessoaId == null || selectedApartamentoId == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Pessoa or Apartamento not found"
                )
                return@launch
            }

            val pessoaResult = getPessoaUseCase(selectedPessoaId)
            val apartamentoResult = getApartamentoUseCase(selectedApartamentoId)

            if(pessoaResult.isFailure || apartamentoResult.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Pessoa or Apartamento not found"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                pessoa = pessoaResult.getOrThrow().toSelectUi(),
                apartamento = apartamentoResult.getOrThrow().toSelectUi(),
                error = null
            )
        }

    }
}