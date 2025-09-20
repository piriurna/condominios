package com.zalamena.condominios.addmorador.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCase
import com.zalamena.condominios.apartamentos.domain.usecase.GetApartamentoUseCase
import com.zalamena.condominios.moradores.ui.mapper.toSelectUi
import com.zalamena.condominios.moradores.ui.models.SelectApartamentoUiData
import com.zalamena.condominios.moradores.ui.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
import kotlinx.coroutines.launch

data class AddMoradorOverviewUiState(
    val isLoading: Boolean = false,
    val pessoa: SelectPessoaUiData? = null,
    val apartamento: SelectApartamentoUiData? = null,
    val error: String? = null,
    val isCompleted: Boolean = false
)

class AddMoradorOverviewViewModel(
    private val getPessoaUseCase: GetPessoaUseCase,
    private val getApartamentoUseCase: GetApartamentoUseCase,
    private val addMoradorUseCase: AddMoradorUseCase
): ViewModel() {
    private val _uiState: MutableState<AddMoradorOverviewUiState> = mutableStateOf(AddMoradorOverviewUiState())
    val uiState: State<AddMoradorOverviewUiState> = _uiState

    fun populateForm(selectedPessoaId: String?, selectedApartamentoId: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

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
                    error = pessoaResult.exceptionOrNull()?.message?:"Error"
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


    fun addMorador() {
        viewModelScope.launch {
            _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val pessoaId = uiState.value.pessoa?.id
            val apartamentoId = uiState.value.apartamento?.id

            if(pessoaId == null || apartamentoId == null) {
                _uiState.value.copy(
                    isLoading = false,
                    error = "Missing Pessoa or Apartamento"
                )
                return@launch
            }

            val addResult = addMoradorUseCase.invoke(pessoaId, apartamentoId)

            if(addResult.isFailure) {
                _uiState.value.copy(
                    isLoading = false,
                    error = addResult.exceptionOrNull()?.message
                )
                return@launch
            }

            _uiState.value.copy(
                isLoading = false,
                error = null,
                isCompleted = true
            )
        }
    }
}