package com.zalamena.condominios.apartamentos.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.apartamentos.domain.usecase.AddApartamentoUseCase
import com.zalamena.condominios.apartamentos.ui.mapper.toDomain
import com.zalamena.condominios.apartamentos.ui.models.AddApartamentoFormUiData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


data class AddApartamentoUiState(
    val addApartamentoForm: AddApartamentoFormUiData = AddApartamentoFormUiData.BLANK,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


class AddApartamentoViewModel(
    private val addApartamentoUseCase: AddApartamentoUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<AddApartamentoUiState> = MutableStateFlow(AddApartamentoUiState())
    val uiState: Flow<AddApartamentoUiState> = _uiState


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
                            errorMessage = null
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