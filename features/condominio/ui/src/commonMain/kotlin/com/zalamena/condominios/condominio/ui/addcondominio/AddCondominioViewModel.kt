package com.zalamena.condominios.condominio.ui.addcondominio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.condominio.usecase.AddCondominioUseCase
import com.zalamena.condominios.condominio.ui.addcondominio.mapper.toDomain
import com.zalamena.condominios.condominio.ui.addcondominio.models.AddCondominioFormUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddCondominioUiState(
    val form: AddCondominioFormUiData = AddCondominioFormUiData.BLANK,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val createdCondominioId: String? = null
)

class AddCondominioViewModel(
    private val addCondominioUseCase: AddCondominioUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCondominioUiState())
    val uiState: StateFlow<AddCondominioUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.update { AddCondominioUiState() }
    }

    fun setNome(nome: String) {
        _uiState.update { it.copy(form = it.form.copy(nome = nome), errorMessage = null) }
    }

    fun setRua(rua: String) {
        _uiState.update { it.copy(form = it.form.copy(rua = rua), errorMessage = null) }
    }

    fun setNumero(numero: String) {
        _uiState.update { it.copy(form = it.form.copy(numero = numero), errorMessage = null) }
    }

    fun setCep(cep: String) {
        _uiState.update { it.copy(form = it.form.copy(cep = cep), errorMessage = null) }
    }

    fun setCidade(cidade: String) {
        _uiState.update { it.copy(form = it.form.copy(cidade = cidade), errorMessage = null) }
    }

    fun setEstado(estado: String) {
        _uiState.update { it.copy(form = it.form.copy(estado = estado), errorMessage = null) }
    }

    fun addCondominio() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (!_uiState.value.form.isValid) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Preencha todos os campos")
                }
                return@launch
            }

            val result = addCondominioUseCase(_uiState.value.form.toDomain())

            when {
                result.isSuccess -> _uiState.update {
                    it.copy(isLoading = false, createdCondominioId = result.getOrThrow())
                }
                result.isFailure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }
}
