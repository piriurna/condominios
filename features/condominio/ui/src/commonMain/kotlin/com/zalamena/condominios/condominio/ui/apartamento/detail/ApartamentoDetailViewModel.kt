package com.zalamena.condominios.condominio.ui.apartamento.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ApartamentoDetailUiState(
    val apartamentoId: String = "",
    val numero: String = "",
    val andar: String = "",
    val moradores: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val navigationEvent: ApartamentoDetailNavEvent? = null
)

sealed class ApartamentoDetailNavEvent {
    data class AddMorador(val apartamentoId: String) : ApartamentoDetailNavEvent()
}

class ApartamentoDetailViewModel(
    private val getApartamentoUseCase: GetApartamentoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApartamentoDetailUiState())
    val uiState: StateFlow<ApartamentoDetailUiState> = _uiState.asStateFlow()

    fun setApartamentoId(apartamentoId: String) {
        _uiState.update { it.copy(apartamentoId = apartamentoId) }
    }

    fun load() {
        val apartamentoId = _uiState.value.apartamentoId
        if (apartamentoId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val result = getApartamentoUseCase(apartamentoId)

            when {
                result.isSuccess -> {
                    val apt = result.getOrThrow()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            numero = apt.numero,
                            andar = apt.andar,
                            moradores = apt.moradores.map { p -> p.nome }
                        )
                    }
                }
                result.isFailure -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            }
        }
    }

    fun onAddMoradorClick() {
        val apartamentoId = _uiState.value.apartamentoId
        if (apartamentoId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = ApartamentoDetailNavEvent.AddMorador(apartamentoId)) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
