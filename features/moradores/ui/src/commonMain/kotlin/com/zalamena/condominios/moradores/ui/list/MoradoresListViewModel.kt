package com.zalamena.condominios.moradores.ui.list

import androidx.lifecycle.ViewModel
import com.zalamena.condominios.moradores.ui.mapper.toUi
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import com.zalamena.moradores.domain.usecase.GetMoradoresUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class MoradoresListUiState(
    val moradores: List<MoradorUiData> = emptyList(),
    val isLoading: Boolean = false
)


class MoradoresListViewModel constructor(
    private val getMoradoresUseCase: GetMoradoresUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<MoradoresListUiState> = MutableStateFlow(MoradoresListUiState())
    val uiState: StateFlow<MoradoresListUiState> = _uiState

    init {
        println("LOGGING START")
    }


    suspend fun getMoradores() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        val moradoresResult = getMoradoresUseCase()

        when {
            moradoresResult.isSuccess -> {
                _uiState.update {
                    it.copy(
                        moradores = moradoresResult.getOrThrow().map { it.toUi() },
                        isLoading = false
                    )
                }

            }

            moradoresResult.isFailure -> {
                _uiState.update {
                    it.copy(
                        moradores = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }
}