package com.zalamena.condominios.condominio.ui.condominio.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// TODO: CHECK IF WE SHOULD USE THE DOMAIN MODEL OR ALWAYS CREATE AN UI MODEL
data class CondominioOverviewUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val condominio: Condominio? = null
)


class CondominioOverviewViewModel constructor(
    private val getCondominioUseCase: GetCondominioUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CondominioOverviewUiState> =
        MutableStateFlow(CondominioOverviewUiState())
    val uiState: StateFlow<CondominioOverviewUiState> = _uiState


    fun fetchData(condominioId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isError = false)

            val condominioResult = getCondominioUseCase.invoke(condominioId)

            when {
                condominioResult.isSuccess -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = false,
                        condominio = condominioResult.getOrThrow()
                    )
                }

                condominioResult.isFailure -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, isError = true)
                }
            }
        }

    }
}