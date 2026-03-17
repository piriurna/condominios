package com.zalamena.condominios.condominio.ui.porteiro.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.common.ui.withMinLoading
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.DeletePorteiroUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.GetPorteirosByCondominioUseCase
import com.zalamena.condominios.condominio.domain.porteiro.usecase.ReassignPorteiroUseCase
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.CondominioSummaryUiData
import com.zalamena.condominios.condominio.ui.condominio.dashboard.toSummaryUi
import com.zalamena.condominios.condominio.ui.porteiro.list.models.PorteiroUiData
import com.zalamena.condominios.condominio.ui.porteiro.list.models.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PorteiroListUiState(
    val condominioId: String = "",
    val porteiros: List<PorteiroUiData> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val showDeleteConfirmation: PorteiroUiData? = null,
    val showReassignDialog: PorteiroUiData? = null,
    val availableCondominios: List<CondominioSummaryUiData> = emptyList()
)

class PorteiroListViewModel(
    private val getPorteirosByCondominioUseCase: GetPorteirosByCondominioUseCase,
    private val deletePorteiroUseCase: DeletePorteiroUseCase,
    private val reassignPorteiroUseCase: ReassignPorteiroUseCase,
    private val getCondominiosUseCase: GetCondominiosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PorteiroListUiState())
    val uiState: StateFlow<PorteiroListUiState> = _uiState

    fun setCondominioId(condominioId: String) {
        _uiState.update { it.copy(condominioId = condominioId) }
    }

    fun loadPorteiros() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return

        viewModelScope.launch {
            val showSpinner = _uiState.value.porteiros.isEmpty()
            _uiState.update { it.copy(isLoading = showSpinner, isError = false) }

            val result = withMinLoading(showSpinner) { getPorteirosByCondominioUseCase(condominioId) }

            when {
                result.isSuccess -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            porteiros = result.getOrThrow().map { info -> info.toUi() }
                        )
                    }
                }
                result.isFailure -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            }
        }
    }

    fun onDeleteClick(porteiro: PorteiroUiData) {
        _uiState.update { it.copy(showDeleteConfirmation = porteiro) }
    }

    fun onConfirmDelete() {
        val porteiro = _uiState.value.showDeleteConfirmation ?: return
        _uiState.update { it.copy(showDeleteConfirmation = null) }

        viewModelScope.launch {
            val result = deletePorteiroUseCase(porteiro.id)
            if (result.isSuccess) {
                loadPorteiros()
            }
        }
    }

    fun onDismissDelete() {
        _uiState.update { it.copy(showDeleteConfirmation = null) }
    }

    fun onReassignClick(porteiro: PorteiroUiData) {
        viewModelScope.launch {
            val condominiosResult = getCondominiosUseCase()
            val condominios = condominiosResult.getOrNull()
                ?.filter { it.id != _uiState.value.condominioId }
                ?.map { it.toSummaryUi() }
                ?: emptyList()

            _uiState.update {
                it.copy(
                    showReassignDialog = porteiro,
                    availableCondominios = condominios
                )
            }
        }
    }

    fun onConfirmReassign(newCondominioId: String) {
        val porteiro = _uiState.value.showReassignDialog ?: return
        _uiState.update { it.copy(showReassignDialog = null) }

        viewModelScope.launch {
            val result = reassignPorteiroUseCase(porteiro.id, newCondominioId)
            if (result.isSuccess) {
                loadPorteiros()
            }
        }
    }

    fun onDismissReassign() {
        _uiState.update { it.copy(showReassignDialog = null, availableCondominios = emptyList()) }
    }
}
