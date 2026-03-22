package com.zalamena.condominios.condominio.ui.moradores.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetMoradoresUseCase
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.maskCpf
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.toLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MoradorSearchUiData(
    val pessoaId: String,
    val nome: String,
    val maskedCpf: String,
    val tipoLabel: String,
    val aptNumero: String,
    val aptAndar: String,
    val apartamentoId: String
)

data class MoradorSearchUiState(
    val query: String = "",
    val moradores: List<MoradorSearchUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val navigationEvent: MoradorSearchNavigationEvent? = null
)

sealed class MoradorSearchNavigationEvent {
    data class MoradorDetail(val pessoaId: String) : MoradorSearchNavigationEvent()
}

class MoradorSearchViewModel(
    private val getMoradoresUseCase: GetMoradoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoradorSearchUiState())
    val uiState: StateFlow<MoradorSearchUiState> = _uiState

    private var allMoradores: List<Morador> = emptyList()
    private var condominioId: String = ""

    fun setCondominioId(id: String) {
        condominioId = id
    }

    fun loadMoradores() {
        if (condominioId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val result = getMoradoresUseCase(condominioId)

            result
                .onSuccess { moradores ->
                    allMoradores = moradores
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            moradores = filterAndMap(moradores, it.query)
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
        }
    }

    fun setQuery(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                moradores = filterAndMap(allMoradores, query)
            )
        }
    }

    fun onMoradorClick(pessoaId: String) {
        _uiState.update {
            it.copy(navigationEvent = MoradorSearchNavigationEvent.MoradorDetail(pessoaId))
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }

    private fun filterAndMap(moradores: List<Morador>, query: String): List<MoradorSearchUiData> {
        val filtered = if (query.isBlank()) {
            moradores
        } else {
            val lowerQuery = query.lowercase()
            moradores.filter { morador ->
                morador.nome.lowercase().contains(lowerQuery) ||
                    morador.apartamento.numero.lowercase().contains(lowerQuery)
            }
        }
        return filtered.map { it.toSearchUiData() }
    }

    private fun Morador.toSearchUiData() = MoradorSearchUiData(
        pessoaId = pessoa.id,
        nome = nome,
        maskedCpf = maskCpf(cpf),
        tipoLabel = tipo.toLabel(),
        aptNumero = apartamento.numero,
        aptAndar = apartamento.andar,
        apartamentoId = apartamento.id
    )
}
