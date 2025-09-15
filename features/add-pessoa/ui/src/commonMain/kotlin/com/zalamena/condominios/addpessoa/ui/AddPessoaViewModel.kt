package com.zalamena.condominios.addpessoa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.addpessoa.domain.usecase.AddPessoaUseCase
import com.zalamena.condominios.addpessoa.ui.mapper.toDomain
import com.zalamena.condominios.addpessoa.ui.models.AddPessoaFormUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddPessoaUiState(
    val isLoading: Boolean = false,
    val addPessoaForm: AddPessoaFormUiData = AddPessoaFormUiData()
)

class AddPessoaViewModel(
    private val addPessoaUseCase: AddPessoaUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<AddPessoaUiState> = MutableStateFlow(AddPessoaUiState())
    val uiState: StateFlow<AddPessoaUiState> = _uiState.asStateFlow()

    fun updateForm(form: AddPessoaFormUiData) {
        _uiState.value = _uiState.value.copy(
            addPessoaForm = form
        )
    }


    fun addPessoa() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val addResult = addPessoaUseCase.invoke(_uiState.value.addPessoaForm.toDomain())

            when {
                addResult.isFailure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }

                addResult.isSuccess -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
            }

        }
    }
}