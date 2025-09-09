package com.zalamena.condominios.moradores.ui

import com.zalamena.condominios.moradores.ui.mapper.toUi
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import com.zalamena.moradores.domain.usecase.GetMoradorUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


data class UiState(
    val morador: MoradorUiData? = null,
    val isLoading: Boolean = false
)

class MoradorInfoViewModel constructor(
    private val getMoradorUseCase: GetMoradorUseCase
) {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    fun getMorador(cpf: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        val moradorResult = getMoradorUseCase(cpf)


        when {
            moradorResult.isSuccess -> {
                _uiState.update {
                    it.copy(
                        morador = moradorResult.getOrThrow().toUi(),
                        isLoading = false
                    )
                }
            }

            moradorResult.isFailure -> {
                _uiState.update {
                    it.copy(
                        morador = null,
                        isLoading = false
                    )
                }
            }
        }
    }
}