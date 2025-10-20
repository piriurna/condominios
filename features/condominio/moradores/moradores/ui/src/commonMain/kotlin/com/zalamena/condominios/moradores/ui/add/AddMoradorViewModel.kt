package com.zalamena.condominios.moradores.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.apartamento.usecase.GetApartamentosUseCase
import com.zalamena.condominios.moradores.ui.mapper.toSelectUi
import com.zalamena.condominios.moradores.ui.models.SelectApartamentoUiData
import com.zalamena.condominios.moradores.ui.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.usecase.GetPessoasListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AddMoradorUiState(
    val pessoasList: List<SelectPessoaUiData> = emptyList(),
    val apartamentosList: List<SelectApartamentoUiData> = emptyList(),
    val pessoaSelected: SelectPessoaUiData? = null,
    val apartamentoSelected: SelectApartamentoUiData? = null,
    val isLoading: Boolean = false
)


class AddMoradorViewModel(
    private val getPessoasListUseCase: GetPessoasListUseCase,
    private val getApartamentosUseCase: GetApartamentosUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<AddMoradorUiState> = MutableStateFlow(AddMoradorUiState())
    val uiState: Flow<AddMoradorUiState> = _uiState


    fun pessoaSelected(pessoa: SelectPessoaUiData) {
        _uiState.value = _uiState.value.copy(pessoaSelected = pessoa)
    }

    fun apartamentoSelected(apartamento: SelectApartamentoUiData) {
        _uiState.value = _uiState.value.copy(apartamentoSelected = apartamento)
    }

    fun populateForm() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            _uiState.update {
                it.copy(
                    pessoasList = getPessoasToSelect(),
                    apartamentosList = getApartamentosToSelect(),
                    isLoading = false
                )
            }
        }
    }

    fun addMorador() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val pessoaId = _uiState.value.pessoaSelected?.id
            val apartamentoId = _uiState.value.apartamentoSelected?.id

            if(pessoaId == null || apartamentoId == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                return@launch
            }


            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }

        }
    }

    private suspend fun getPessoasToSelect(): List<SelectPessoaUiData> {
        val pessoasResult = getPessoasListUseCase()

        if(pessoasResult.isFailure) {
            return emptyList()
        }

        return pessoasResult.getOrThrow().map {
            it.toSelectUi()
        }
    }

    private suspend fun getApartamentosToSelect(): List<SelectApartamentoUiData> {
        val apartamentoResult = getApartamentosUseCase()

        if(apartamentoResult.isFailure) {
            return emptyList()
        }

        return apartamentoResult.getOrThrow().map {
            it.toSelectUi()
        }
    }
}