package com.zalamena.condominios.moradores.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.moradores.ui.mapper.toUi
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.moradores.domain.usecase.AddMoradorUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MoradoresListUiState(
    val moradores: List<MoradorUiData> = emptyList(),
    val isLoading: Boolean = false
)


class MoradoresListViewModel constructor(
    private val getMoradoresUseCase: GetMoradoresUseCase,
    private val addMoradorUseCase: AddMoradorUseCase
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


    fun addMoradorClicked() {
        viewModelScope.launch {
            println("addMoradorClicked")

            addMoradorUseCase.invoke(
                Pessoa.dummy,
                Apartamento.dummy
            ).onSuccess {
                println("success")
            }
                .onFailure {
                    println("failure")
                }
        }
    }
}