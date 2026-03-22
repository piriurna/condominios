package com.zalamena.condominios.condominio.ui.addmorador.overview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.usecase.AddMoradorUseCase
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.ui.moradores.mapper.toSelectUi
import com.zalamena.condominios.condominio.ui.moradores.models.SelectApartamentoUiData
import com.zalamena.condominios.condominio.ui.moradores.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoaUseCase
import kotlinx.coroutines.launch

sealed class AddMoradorOverviewNavEvent {
    data object Completed : AddMoradorOverviewNavEvent()
}

data class AddMoradorOverviewUiState(
    val isLoading: Boolean = false,
    val pessoa: SelectPessoaUiData? = null,
    val apartamento: SelectApartamentoUiData? = null,
    val selectedTipo: MoradorTipo = MoradorTipo.RESIDENTE,
    val error: String? = null,
    val navigationEvent: AddMoradorOverviewNavEvent? = null
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
                isLoading = true,
                error = null
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
                    error = pessoaResult.exceptionOrNull()?.message
                        ?:apartamentoResult.exceptionOrNull()?.message
                        ?:"Pessoa or Apartamento not found"
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


    fun setTipo(tipo: MoradorTipo) {
        _uiState.value = _uiState.value.copy(selectedTipo = tipo)
    }

    fun addMorador() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val pessoaId = uiState.value.pessoa?.id
            val apartamentoId = uiState.value.apartamento?.id

            if(pessoaId == null || apartamentoId == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Missing Pessoa or Apartamento"
                )
                return@launch
            }

            val addResult = addMoradorUseCase.invoke(pessoaId, apartamentoId, uiState.value.selectedTipo)

            if(addResult.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = addResult.exceptionOrNull()?.message
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = null,
                navigationEvent = AddMoradorOverviewNavEvent.Completed
            )
        }
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(navigationEvent = null)
    }
}