package com.zalamena.condominios.condominio.ui.condominio.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.ApartamentoDashboardUiData
import com.zalamena.condominios.common.ui.withMinLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CondominioDashboardUiState(
    val condominioId: String = "",
    val condominioNome: String = "",
    val apartamentos: List<ApartamentoDashboardUiData> = emptyList(),
    val totalApartamentos: Int = 0,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val isAdminMode: Boolean = true,
    val navigationEvent: DashboardNavigationEvent? = null
)

sealed class DashboardNavigationEvent {
    data class AddApartamento(val condominioId: String) : DashboardNavigationEvent()
    data class ApartamentoDetails(val apartamentoId: String) : DashboardNavigationEvent()
    data class CreatePorteiro(val condominioId: String) : DashboardNavigationEvent()
    data class PorteiroList(val condominioId: String) : DashboardNavigationEvent()
    data class SearchMorador(val condominioId: String) : DashboardNavigationEvent()
    data class AmenityList(val condominioId: String) : DashboardNavigationEvent()
    object Logout : DashboardNavigationEvent()
}

class CondominioDashboardViewModel(
    private val getCondominiosUseCase: GetCondominiosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CondominioDashboardUiState())
    val uiState: StateFlow<CondominioDashboardUiState> = _uiState

    fun setCondominioId(condominioId: String) {
        _uiState.update { it.copy(condominioId = condominioId) }
    }

    fun setAdminMode(isAdmin: Boolean) {
        _uiState.update { it.copy(isAdminMode = isAdmin) }
    }

    fun loadCondominios() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return

        viewModelScope.launch {
            val showSpinner = _uiState.value.apartamentos.isEmpty()
            _uiState.update { it.copy(isLoading = showSpinner, isError = false) }

            val result = withMinLoading(showSpinner) { getCondominiosUseCase() }

            when {
                result.isSuccess -> {
                    val condominio = result.getOrThrow().find { it.id == condominioId }
                    if (condominio != null) {
                        val apartamentos = condominio.apartamentos.map { it.toDashboardUi() }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                condominioNome = condominio.nome,
                                apartamentos = apartamentos,
                                totalApartamentos = apartamentos.size
                            )
                        }
                    } else {
                        if (!_uiState.value.isAdminMode) {
                            _uiState.update { it.copy(isLoading = false, navigationEvent = DashboardNavigationEvent.Logout) }
                        } else {
                            _uiState.update { it.copy(isLoading = false, isError = true) }
                        }
                    }
                }
                result.isFailure -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            }
        }
    }

    fun onAddApartamentoClick() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = DashboardNavigationEvent.AddApartamento(condominioId)) }
    }

    fun onCreatePorteiroClick() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = DashboardNavigationEvent.CreatePorteiro(condominioId)) }
    }

    fun onApartamentoClick(apartamentoId: String) {
        _uiState.update {
            it.copy(navigationEvent = DashboardNavigationEvent.ApartamentoDetails(apartamentoId))
        }
    }

    fun onPorteiroListClick() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = DashboardNavigationEvent.PorteiroList(condominioId)) }
    }

    fun onSearchMoradorClick() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = DashboardNavigationEvent.SearchMorador(condominioId)) }
    }

    fun onAmenityListClick() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = DashboardNavigationEvent.AmenityList(condominioId)) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
