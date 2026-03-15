package com.zalamena.condominios.condominio.ui.condominio.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.ApartamentoDashboardUiData
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.CondominioSummaryUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CondominioDashboardUiState(
    val condominios: List<CondominioSummaryUiData> = emptyList(),
    val selectedCondominioId: String? = null,
    val apartamentos: List<ApartamentoDashboardUiData> = emptyList(),
    val totalApartamentos: Int = 0,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val navigationEvent: DashboardNavigationEvent? = null
)

sealed class DashboardNavigationEvent {
    data class AddApartamento(val condominioId: String) : DashboardNavigationEvent()
    data class ApartamentoDetails(val apartamentoId: String) : DashboardNavigationEvent()
}

class CondominioDashboardViewModel(
    private val getCondominiosUseCase: GetCondominiosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CondominioDashboardUiState())
    val uiState: StateFlow<CondominioDashboardUiState> = _uiState

    private var loadedCondominios: List<Condominio> = emptyList()

    fun loadCondominios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val result = getCondominiosUseCase()

            when {
                result.isSuccess -> {
                    loadedCondominios = result.getOrThrow()
                    val currentSelectedId = _uiState.value.selectedCondominioId
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            condominios = loadedCondominios.map { c -> c.toSummaryUi() }
                        )
                    }
                    if (currentSelectedId != null) {
                        selectCondominio(currentSelectedId)
                    }
                }
                result.isFailure -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            }
        }
    }

    fun selectCondominio(condominioId: String) {
        val condominio = loadedCondominios.find { it.id == condominioId } ?: return
        val apartamentos = condominio.apartamentos.map { it.toDashboardUi() }

        _uiState.update {
            it.copy(
                selectedCondominioId = condominioId,
                apartamentos = apartamentos,
                totalApartamentos = apartamentos.size
            )
        }
    }

    fun onAddApartamentoClick() {
        val condominioId = _uiState.value.selectedCondominioId ?: return
        _uiState.update { it.copy(navigationEvent = DashboardNavigationEvent.AddApartamento(condominioId)) }
    }

    fun onApartamentoClick(apartamentoId: String) {
        _uiState.update {
            it.copy(navigationEvent = DashboardNavigationEvent.ApartamentoDetails(apartamentoId))
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
