package com.zalamena.condominios.navigation.ui.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.usecase.GetAmenitiesByCondominioUseCase
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MoradorShellUiState(
    val condominioName: String = "",
    val amenities: List<Amenity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MoradorShellViewModel(
    private val getCondominioUseCase: GetCondominioUseCase,
    private val getAmenitiesByCondominioUseCase: GetAmenitiesByCondominioUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoradorShellUiState())
    val uiState: StateFlow<MoradorShellUiState> = _uiState

    private var condominioId: String = ""

    fun setCondominioId(id: String) {
        condominioId = id
    }

    fun loadCondominio() {
        if (condominioId.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getCondominioUseCase(condominioId)
                .onSuccess { condominio ->
                    _uiState.update {
                        it.copy(
                            condominioName = condominio.nome,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Erro ao carregar condominio"
                        )
                    }
                }

            getAmenitiesByCondominioUseCase(condominioId)
                .onSuccess { amenities ->
                    _uiState.update { it.copy(amenities = amenities) }
                }
        }
    }
}
