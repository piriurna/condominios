package com.zalamena.condominios.condominio.ui.addmorador.flowController

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class AddMoradorFlowUiState(
    val createdPessoaId: String? = null,
    val createdApartamentoId: String? = null
)


class AddMoradorFlowViewModel: ViewModel() {

    private val _uiState: MutableState<AddMoradorFlowUiState> = mutableStateOf(AddMoradorFlowUiState())
    val uiState: State<AddMoradorFlowUiState> = _uiState


    fun setCreatedPessoaId(id: String) {
        _uiState.value = _uiState.value.copy(
            createdPessoaId = id
        )
    }

    fun setCreatedApartamentoId(id: String) {
        _uiState.value = _uiState.value.copy(
            createdApartamentoId = id
        )
    }

}