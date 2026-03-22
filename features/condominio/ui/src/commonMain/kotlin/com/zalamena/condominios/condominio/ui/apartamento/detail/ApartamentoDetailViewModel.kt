package com.zalamena.condominios.condominio.ui.apartamento.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentoUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradoresForApartamentoUseCase
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.MoradorDetailUiData
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.maskCpf
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.toLabel
import com.zalamena.condominios.common.ui.withMinLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ApartamentoDetailUiState(
    val apartamentoId: String = "",
    val numero: String = "",
    val andar: String = "",
    val moradores: List<MoradorDetailUiData> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val navigationEvent: ApartamentoDetailNavEvent? = null
)

sealed class ApartamentoDetailNavEvent {
    data class AddMorador(val apartamentoId: String) : ApartamentoDetailNavEvent()
    data class MoradorDetail(val pessoaId: String) : ApartamentoDetailNavEvent()
}

class ApartamentoDetailViewModel(
    private val getApartamentoUseCase: GetApartamentoUseCase,
    private val getMoradoresForApartamentoUseCase: GetMoradoresForApartamentoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApartamentoDetailUiState())
    val uiState: StateFlow<ApartamentoDetailUiState> = _uiState.asStateFlow()

    fun setApartamentoId(apartamentoId: String) {
        _uiState.update { it.copy(apartamentoId = apartamentoId) }
    }

    fun load() {
        val apartamentoId = _uiState.value.apartamentoId
        if (apartamentoId.isBlank()) return

        viewModelScope.launch {
            val showSpinner = _uiState.value.numero.isBlank()
            _uiState.update { it.copy(isLoading = showSpinner, isError = false) }

            val (aptResult, moradoresResult) = withMinLoading(showSpinner) {
                Pair(getApartamentoUseCase(apartamentoId), getMoradoresForApartamentoUseCase(apartamentoId))
            }

            when {
                aptResult.isSuccess && moradoresResult.isSuccess -> {
                    val apt = aptResult.getOrThrow()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            numero = apt.numero,
                            andar = apt.andar,
                            moradores = moradoresResult.getOrThrow().map { m ->
                                MoradorDetailUiData(
                                    pessoaId = m.pessoa.id,
                                    nome = m.nome,
                                    maskedCpf = maskCpf(m.cpf),
                                    tipoLabel = m.tipo.toLabel()
                                )
                            }
                        )
                    }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            }
        }
    }

    fun onMoradorClick(pessoaId: String) {
        _uiState.update { it.copy(navigationEvent = ApartamentoDetailNavEvent.MoradorDetail(pessoaId)) }
    }

    fun onAddMoradorClick() {
        val apartamentoId = _uiState.value.apartamentoId
        if (apartamentoId.isBlank()) return
        _uiState.update { it.copy(navigationEvent = ApartamentoDetailNavEvent.AddMorador(apartamentoId)) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
