package com.zalamena.condominios.condominio.ui.moradores.list

import androidx.lifecycle.ViewModel
import com.zalamena.condominios.condominio.domain.moradores.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.ui.moradores.mapper.toUi
import com.zalamena.condominios.condominio.ui.moradores.models.MoradorUiData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class MoradoresListUiState(
    val moradores: List<MoradorUiData> = emptyList(),
    val isLoading: Boolean = false
)


class MoradoresListViewModel(
    private val getMoradoresUseCase: GetMoradoresUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<MoradoresListUiState> = MutableStateFlow(MoradoresListUiState())
    val uiState: Flow<MoradoresListUiState> = _uiState

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