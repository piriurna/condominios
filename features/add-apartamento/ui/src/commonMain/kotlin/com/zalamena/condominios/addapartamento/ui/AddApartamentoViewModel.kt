package com.zalamena.condominios.addapartamento.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.addapartamento.domain.usecases.AddApartamentoUseCase
import com.zalamena.condominios.addapartamento.ui.mapper.toDomain
import com.zalamena.condominios.addapartamento.ui.models.AddApartamentoFormUiData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


data class AddApartamentoUiState(
    val addApartamentoForm: AddApartamentoFormUiData = AddApartamentoFormUiData.BLANK,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val createdApartamentoId: String? = null
)


class AddApartamentoViewModel(
    private val addApartamentoUseCase: AddApartamentoUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<AddApartamentoUiState> = MutableStateFlow(AddApartamentoUiState())
    val uiState: StateFlow<AddApartamentoUiState> = _uiState.asStateFlow()


    fun setNumeroApartamento(numeroApartamento: String) {
        val fixedNumero = numeroApartamento.trim().filter { it.isDigit() }
        _uiState.update {
            it.copy(
                addApartamentoForm = it.addApartamentoForm.copy(
                    numero = fixedNumero
                ),
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
                errorMessage = null
            )
        }
    }


    fun addApartamento() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            delay(3.seconds)

            if(!formValid()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Preencha todos os campos"
                    )
                }

                return@launch
            }

            val form = _uiState.value.addApartamentoForm.toDomain()

            val addResult = addApartamentoUseCase(form)

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