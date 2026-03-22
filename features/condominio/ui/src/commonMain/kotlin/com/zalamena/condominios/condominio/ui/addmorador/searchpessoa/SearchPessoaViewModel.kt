package com.zalamena.condominios.condominio.ui.addmorador.searchpessoa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.morador.usecase.GetAvailablePessoasForApartamentoUseCase
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.maskCpf
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchPessoaUiData(
    val pessoaId: String,
    val nome: String,
    val maskedCpf: String
)

data class SearchPessoaUiState(
    val query: String = "",
    val pessoas: List<SearchPessoaUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val navigationEvent: SearchPessoaNavigationEvent? = null
)

sealed class SearchPessoaNavigationEvent {
    data class SelectPessoa(val pessoaId: String) : SearchPessoaNavigationEvent()
    data object CreateNewPessoa : SearchPessoaNavigationEvent()
}

class SearchPessoaViewModel(
    private val getAvailablePessoasForApartamentoUseCase: GetAvailablePessoasForApartamentoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchPessoaUiState())
    val uiState: StateFlow<SearchPessoaUiState> = _uiState

    private var allPessoas: List<Pessoa> = emptyList()
    private var apartamentoId: String? = null

    fun setApartamentoId(id: String?) {
        apartamentoId = id
    }

    fun loadPessoas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            getAvailablePessoasForApartamentoUseCase(apartamentoId)
                .onSuccess { pessoas ->
                    allPessoas = pessoas
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pessoas = filterAndMap(pessoas, it.query)
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
                pessoas = filterAndMap(allPessoas, query)
            )
        }
    }

    fun onPessoaClick(pessoaId: String) {
        _uiState.update {
            it.copy(navigationEvent = SearchPessoaNavigationEvent.SelectPessoa(pessoaId))
        }
    }

    fun onCreateNewPessoaClick() {
        _uiState.update {
            it.copy(navigationEvent = SearchPessoaNavigationEvent.CreateNewPessoa)
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }

    private fun filterAndMap(pessoas: List<Pessoa>, query: String): List<SearchPessoaUiData> {
        val filtered = if (query.isBlank()) {
            pessoas
        } else {
            val lowerQuery = query.lowercase()
            val digitQuery = query.filter { it.isDigit() }
            pessoas.filter { pessoa ->
                pessoa.nome.lowercase().contains(lowerQuery) ||
                    (digitQuery.isNotEmpty() && pessoa.cpf.contains(digitQuery))
            }
        }
        return filtered.map { it.toSearchUiData() }
    }

    private fun Pessoa.toSearchUiData() = SearchPessoaUiData(
        pessoaId = id,
        nome = nome,
        maskedCpf = maskCpf(cpf)
    )
}
