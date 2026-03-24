package com.zalamena.condominios.condominio.ui.addapartamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.apartamento.usecase.AddApartamentoUseCase
import com.zalamena.condominios.condominio.ui.addapartamento.mapper.toDomain
import com.zalamena.condominios.condominio.ui.addapartamento.models.AddApartamentoFormUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AddApartamentoUiState(
    val condominioId: String = "",
    val addApartamentoForm: AddApartamentoFormUiData = AddApartamentoFormUiData.BLANK,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val numeroError: String? = null,
    val andarError: String? = null,
    val createdApartamentoId: String? = null,
    val andarManuallyEdited: Boolean = false
)


class AddApartamentoViewModel(
    private val addApartamentoUseCase: AddApartamentoUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<AddApartamentoUiState> = MutableStateFlow(AddApartamentoUiState())
    val uiState: StateFlow<AddApartamentoUiState> = _uiState.asStateFlow()


    fun reset() {
        _uiState.update { AddApartamentoUiState(condominioId = it.condominioId) }
    }

    fun setCondominioId(condominioId: String) {
        _uiState.update { it.copy(condominioId = condominioId) }
    }

    fun setNumeroApartamento(numeroApartamento: String) {
        val fixedNumero = numeroApartamento.trim().filter { it.isDigit() }
        _uiState.update {
            val suggestedAndar = if (!it.andarManuallyEdited && fixedNumero.length >= 2) {
                fixedNumero.dropLast(2)
            } else if (!it.andarManuallyEdited) {
                ""
            } else {
                it.addApartamentoForm.andar
            }
            it.copy(
                addApartamentoForm = it.addApartamentoForm.copy(
                    numero = fixedNumero,
                    andar = suggestedAndar
                ),
                numeroError = null,
                andarError = null,
                errorMessage = null
            )
        }
    }

    fun setAndarApartamento(andarApartamento: String) {
        val fixedAndar = andarApartamento.trim().filter { it.isDigit() }
        _uiState.update {
            it.copy(
                addApartamentoForm = it.addApartamentoForm.copy(
                    andar = fixedAndar
                ),
                andarError = null,
                andarManuallyEdited = true,
                errorMessage = null
            )
        }
    }


    fun addApartamento() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            val form = _uiState.value.addApartamentoForm
            val numErr = if (form.numero.isBlank()) "Número é obrigatório" else null
            val andErr = if (form.andar.isBlank()) "Andar é obrigatório" else null

            if (numErr != null || andErr != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        numeroError = numErr,
                        andarError = andErr,
                        errorMessage = null
                    )
                }
                return@launch
            }

            val domainForm = _uiState.value.addApartamentoForm.toDomain(_uiState.value.condominioId)

            val addResult = addApartamentoUseCase(domainForm)

            when {
                addResult.isFailure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = addResult.exceptionOrNull()?.message
                        )
                    }
                }

                addResult.isSuccess -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            createdApartamentoId = addResult.getOrThrow()
                        )
                    }
                }
            }
        }
    }


    private fun formValid(): Boolean {
        return _uiState.value.addApartamentoForm.isValid
    }
}