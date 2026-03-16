package com.zalamena.condominios.condominio.ui.adminhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.CondominioSummaryUiData
import com.zalamena.condominios.condominio.ui.condominio.dashboard.toSummaryUi
import com.zalamena.condominios.common.ui.withMinLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminHomeUiState(
    val condominios: List<CondominioSummaryUiData> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val navigationEvent: AdminHomeNavigationEvent? = null
)

sealed class AdminHomeNavigationEvent {
    data object AddCondominio : AdminHomeNavigationEvent()
    data class CondominioDetails(val condominioId: String) : AdminHomeNavigationEvent()
}

class AdminHomeViewModel(
    private val getCondominiosUseCase: GetCondominiosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminHomeUiState())
    val uiState: StateFlow<AdminHomeUiState> = _uiState

    fun loadCondominios() {
        viewModelScope.launch {
            val showSpinner = _uiState.value.condominios.isEmpty()
            _uiState.update { it.copy(isLoading = showSpinner, isError = false) }

            val result = withMinLoading(showSpinner) { getCondominiosUseCase() }

            when {
                result.isSuccess -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            condominios = result.getOrThrow().map { c -> c.toSummaryUi() }
                        )
                    }
                }
                result.isFailure -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            }
        }
    }

    fun onCondominioClick(condominioId: String) {
        _uiState.update {
            it.copy(navigationEvent = AdminHomeNavigationEvent.CondominioDetails(condominioId))
        }
    }

    fun onAddCondominioClick() {
        _uiState.update {
            it.copy(navigationEvent = AdminHomeNavigationEvent.AddCondominio)
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
