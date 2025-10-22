package com.zalamena.condominios.pessoa.ui.addpessoa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaException
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaFormError
import com.zalamena.condominios.pessoa.domain.addpessoa.usecase.AddPessoaUseCase
import com.zalamena.condominios.pessoa.ui.addpessoa.mapper.toDomain
import com.zalamena.condominios.pessoa.ui.addpessoa.models.AddPessoaFormUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddPessoaUiState(
    val addPessoaForm: AddPessoaFormUiData = AddPessoaFormUiData(),
    val isLoading: Boolean = false,
    val formErrors: List<AddPessoaFormError> = emptyList(),
    val createdPessoaId: String? = null
)

class AddPessoaViewModel(
    private val addPessoaUseCase: AddPessoaUseCase
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
            _uiState.value = _uiState.value.copy(isLoading = true, formErrors = emptyList())

            val form = _uiState.value.addPessoaForm.toDomain()

            val addResult = addPessoaUseCase.invoke(form)

            when {
                addResult.isSuccess -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        formErrors = emptyList(),
                        createdPessoaId = addResult.getOrThrow()
                    )
                }


                addResult.exceptionOrNull() is AddPessoaException.FormValidationException -> {
                    val formValidationException = addResult.exceptionOrNull() as AddPessoaException.FormValidationException

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        formErrors = formValidationException.errors
                    )
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        formErrors = emptyList()
                    )
                }
            }

        }
    }
}