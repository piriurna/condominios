package com.zalamena.condominios.condominio.ui.amenity.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.usecase.GetAmenitiesByCondominioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AmenityListUiState(
    val condominioId: String = "",
    val amenities: List<Amenity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigationEvent: AmenityListNavigationEvent? = null
)

sealed class AmenityListNavigationEvent {
    data class AddAmenity(val condominioId: String) : AmenityListNavigationEvent()
}

class AmenityListViewModel(
    private val getAmenitiesByCondominioUseCase: GetAmenitiesByCondominioUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AmenityListUiState())
    val uiState: StateFlow<AmenityListUiState> = _uiState.asStateFlow()

    fun setCondominioId(condominioId: String) {
        _uiState.update { it.copy(condominioId = condominioId) }
    }

    fun loadAmenities() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getAmenitiesByCondominioUseCase(condominioId)
                .onSuccess { amenities ->
                    _uiState.update {
                        it.copy(isLoading = false, amenities = amenities)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
        }
    }

    fun onAddAmenityClick() {
        val condominioId = _uiState.value.condominioId
        if (condominioId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = AmenityListNavigationEvent.AddAmenity(condominioId)) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
