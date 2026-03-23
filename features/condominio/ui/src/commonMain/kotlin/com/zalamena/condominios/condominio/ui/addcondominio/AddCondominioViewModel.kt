package com.zalamena.condominios.condominio.ui.addcondominio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.address.CepLookupProvider
import com.zalamena.condominios.condominio.domain.address.CidadeProvider
import com.zalamena.condominios.condominio.domain.address.Cidade
import com.zalamena.condominios.condominio.domain.address.Estado
import com.zalamena.condominios.condominio.domain.address.EstadoProvider
import com.zalamena.condominios.condominio.domain.condominio.usecase.AddCondominioUseCase
import com.zalamena.condominios.condominio.ui.addcondominio.mapper.toDomain
import com.zalamena.condominios.condominio.ui.addcondominio.models.AddCondominioFormUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddCondominioUiState(
    val form: AddCondominioFormUiData = AddCondominioFormUiData.BLANK,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val nomeError: String? = null,
    val ruaError: String? = null,
    val numeroError: String? = null,
    val cepError: String? = null,
    val cidadeError: String? = null,
    val estadoError: String? = null,
    val createdCondominioId: String? = null,
    val estados: List<Estado> = emptyList(),
    val cidades: List<Cidade> = emptyList(),
    val isLoadingEstados: Boolean = false,
    val isLoadingCidades: Boolean = false,
    val isLoadingCep: Boolean = false,
    val cepMessage: String? = null,
    val useDropdowns: Boolean = true
)

class AddCondominioViewModel(
    private val addCondominioUseCase: AddCondominioUseCase,
    private val estadoProvider: EstadoProvider,
    private val cidadeProvider: CidadeProvider,
    private val cepLookupProvider: CepLookupProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCondominioUiState())
    val uiState: StateFlow<AddCondominioUiState> = _uiState.asStateFlow()

    init {
        loadEstados()
    }

    private fun loadEstados() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingEstados = true) }
            estadoProvider.getEstados()
                .onSuccess { estados ->
                    _uiState.update { it.copy(estados = estados, isLoadingEstados = false, useDropdowns = true) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingEstados = false, useDropdowns = false) }
                }
        }
    }

    private fun loadCidades(uf: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCidades = true, cidades = emptyList()) }
            cidadeProvider.getCidades(uf)
                .onSuccess { cidades ->
                    _uiState.update { it.copy(cidades = cidades, isLoadingCidades = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingCidades = false) }
                }
        }
    }

    private fun lookupCep(cep: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCep = true, cepMessage = null) }
            cepLookupProvider.lookup(cep)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isLoadingCep = false,
                            cepMessage = null,
                            form = it.form.copy(
                                rua = result.rua,
                                cidade = result.cidade,
                                estado = result.estado
                            ),
                            ruaError = null,
                            cidadeError = null,
                            estadoError = null
                        )
                    }
                    // If using dropdowns and estado was set, load cidades for that estado
                    if (_uiState.value.useDropdowns && result.estado.isNotEmpty()) {
                        loadCidades(result.estado)
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingCep = false, cepMessage = "CEP nao encontrado") }
                }
        }
    }

    fun reset() {
        _uiState.update { AddCondominioUiState() }
        loadEstados()
    }

    fun setNome(nome: String) {
        _uiState.update { it.copy(form = it.form.copy(nome = nome), nomeError = null, errorMessage = null) }
    }

    fun setRua(rua: String) {
        _uiState.update { it.copy(form = it.form.copy(rua = rua), ruaError = null, errorMessage = null) }
    }

    fun setNumero(numero: String) {
        _uiState.update { it.copy(form = it.form.copy(numero = numero), numeroError = null, errorMessage = null) }
    }

    fun setCep(cep: String) {
        _uiState.update { it.copy(form = it.form.copy(cep = cep), cepError = null, errorMessage = null, cepMessage = null) }
        val digits = cep.filter { it.isDigit() }
        if (digits.length == 8) {
            lookupCep(digits)
        }
    }

    fun setCidade(cidade: String) {
        _uiState.update { it.copy(form = it.form.copy(cidade = cidade), cidadeError = null, errorMessage = null) }
    }

    fun setEstado(estado: String) {
        _uiState.update {
            it.copy(
                form = it.form.copy(estado = estado, cidade = ""),
                estadoError = null,
                errorMessage = null,
                cidades = emptyList()
            )
        }
        if (_uiState.value.useDropdowns && estado.isNotBlank()) {
            loadCidades(estado)
        }
    }

    fun addCondominio() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val form = _uiState.value.form
            val nomeErr = if (form.nome.isBlank()) "Nome e obrigatorio" else null
            val ruaErr = if (form.rua.isBlank()) "Rua e obrigatoria" else null
            val numeroErr = if (form.numero.isBlank()) "Numero e obrigatorio" else null
            val cidadeErr = if (form.cidade.isBlank()) "Cidade e obrigatoria" else null
            val estadoErr = if (form.estado.isBlank()) "Estado e obrigatorio" else null
            val cepDigits = form.cep.filter { it.isDigit() }
            val cepErr = when {
                form.cep.isBlank() -> "CEP e obrigatorio"
                cepDigits.length != 8 -> "CEP deve ter 8 digitos"
                else -> null
            }

            val hasErrors = listOf(nomeErr, ruaErr, numeroErr, cepErr, cidadeErr, estadoErr).any { it != null }

            if (hasErrors) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nomeError = nomeErr,
                        ruaError = ruaErr,
                        numeroError = numeroErr,
                        cepError = cepErr,
                        cidadeError = cidadeErr,
                        estadoError = estadoErr,
                        errorMessage = null
                    )
                }
                return@launch
            }

            val result = addCondominioUseCase(_uiState.value.form.toDomain())

            when {
                result.isSuccess -> _uiState.update {
                    it.copy(isLoading = false, createdCondominioId = result.getOrThrow())
                }
                result.isFailure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }
}
