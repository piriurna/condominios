package com.zalamena.condominios.condominio.ui.amenity.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.usecase.AddAmenityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddAmenityUiState(
    val condominioId: String = "",
    val nome: String = "",
    val descricao: String = "",
    val capacidade: String = "",
    val precoUso: String = "",
    val requerAprovacaoSindico: Boolean = false,
    val nomeError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: AddAmenityNavigationEvent? = null
)

sealed class AddAmenityNavigationEvent {
    object Success : AddAmenityNavigationEvent()
}

class AddAmenityViewModel(
    private val addAmenityUseCase: AddAmenityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAmenityUiState())
    val uiState: StateFlow<AddAmenityUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.update { AddAmenityUiState() }
    }

    fun setCondominioId(condominioId: String) {
        _uiState.update { it.copy(condominioId = condominioId) }
    }

    fun setNome(nome: String) {
        _uiState.update { it.copy(nome = nome, nomeError = null, errorMessage = null) }
    }

    fun setDescricao(descricao: String) {
        _uiState.update { it.copy(descricao = descricao) }
    }

    fun setCapacidade(capacidade: String) {
        val filtered = capacidade.filter { it.isDigit() }
        _uiState.update { it.copy(capacidade = filtered) }
    }

    fun setPrecoUso(precoUso: String) {
        val filtered = precoUso.filter { it.isDigit() }
        _uiState.update { it.copy(precoUso = filtered) }
    }

    fun setRequerAprovacaoSindico(requer: Boolean) {
        _uiState.update { it.copy(requerAprovacaoSindico = requer) }
    }

    fun submit() {
        val state = _uiState.value

        if (state.nome.isBlank()) {
            _uiState.update { it.copy(nomeError = "Nome e obrigatorio") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val amenity = Amenity(
                id = "",
                condominioId = state.condominioId,
                nome = state.nome.trim(),
                descricao = state.descricao.trim(),
                capacidade = state.capacidade.toIntOrNull(),
                precoUso = state.precoUso.toLongOrNull()?.let { it / 100.0 },
                requerAprovacaoSindico = state.requerAprovacaoSindico
            )

            val result = addAmenityUseCase(amenity)

            when {
                result.isSuccess -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            navigationEvent = AddAmenityNavigationEvent.Success
                        )
                    }
                }
                result.isFailure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message
                        )
                    }
                }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
