package com.zalamena.condominios.condominio.ui.moradores.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorDetailUseCase
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.maskCpf
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.toLabel
import com.zalamena.condominios.common.ui.withMinLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ApartamentoDoMoradorUiData(
    val apartamentoId: String,
    val numero: String,
    val andar: String,
    val tipoLabel: String
)

data class MoradorDetailUiState(
    val nome: String = "",
    val maskedCpf: String = "",
    val email: String = "",
    val telefone: String = "",
    val apartamentos: List<ApartamentoDoMoradorUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class MoradorInfoViewModel(
    private val getMoradorDetailUseCase: GetMoradorDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoradorDetailUiState())
    val uiState: StateFlow<MoradorDetailUiState> = _uiState.asStateFlow()

    private var pessoaId: String = ""

    fun setPessoaId(pessoaId: String) {
        this.pessoaId = pessoaId
    }

    fun load() {
        if (pessoaId.isBlank()) return

        viewModelScope.launch {
            val showSpinner = _uiState.value.nome.isBlank()
            _uiState.update { it.copy(isLoading = showSpinner, isError = false) }

            val result = withMinLoading(showSpinner) {
                getMoradorDetailUseCase(pessoaId)
            }

            result
                .onSuccess { moradores ->
                    if (moradores.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false, isError = true) }
                        return@launch
                    }

                    val first = moradores.first()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nome = first.nome,
                            maskedCpf = maskCpf(first.cpf),
                            email = first.email.ifBlank { "Nao informado" },
                            telefone = first.telefone.ifBlank { "Nao informado" },
                            apartamentos = moradores.map { m ->
                                ApartamentoDoMoradorUiData(
                                    apartamentoId = m.apartamento.id,
                                    numero = m.apartamento.numero,
                                    andar = m.apartamento.andar,
                                    tipoLabel = m.tipo.toLabel()
                                )
                            }
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
        }
    }
}
