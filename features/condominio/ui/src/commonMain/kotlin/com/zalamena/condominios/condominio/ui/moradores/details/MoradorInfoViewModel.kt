package com.zalamena.condominios.condominio.ui.moradores.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradorAccountCreator
import com.zalamena.condominios.condominio.domain.morador.repository.MoradorAccountProvider
import com.zalamena.condominios.condominio.domain.morador.usecase.GetMoradorDetailUseCase
import com.zalamena.condominios.condominio.domain.morador.usecase.UpdateMoradorTipoUseCase
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
    val tipoLabel: String,
    val tipo: MoradorTipo = MoradorTipo.RESIDENTE
)

sealed class MoradorInfoNavigationEvent {
    data class ApartamentoDetails(val apartamentoId: String) : MoradorInfoNavigationEvent()
}

data class MoradorDetailUiState(
    val nome: String = "",
    val maskedCpf: String = "",
    val email: String = "",
    val telefone: String = "",
    val apartamentos: List<ApartamentoDoMoradorUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val navigationEvent: MoradorInfoNavigationEvent? = null,
    val isAdminMode: Boolean = false,
    val tipoError: String? = null,
    // Account-related state
    val accountEmail: String? = null,
    val showCreateAccountDialog: Boolean = false,
    val createAccountEmail: String = "",
    val createAccountPassword: String = "",
    val createAccountEmailError: String? = null,
    val createAccountPasswordError: String? = null,
    val createAccountError: String? = null,
    val isCreatingAccount: Boolean = false,
    val accountCreatedSuccess: Boolean = false
)

class MoradorInfoViewModel(
    private val getMoradorDetailUseCase: GetMoradorDetailUseCase,
    private val updateMoradorTipoUseCase: UpdateMoradorTipoUseCase,
    private val moradorAccountProvider: MoradorAccountProvider,
    private val moradorAccountCreator: MoradorAccountCreator
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
                                    tipoLabel = m.tipo.toLabel(),
                                    tipo = m.tipo
                                )
                            }
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }

            // Load account info
            loadAccountInfo()
        }
    }

    private suspend fun loadAccountInfo() {
        val email = moradorAccountProvider.getAccountEmailForPessoa(pessoaId)
        _uiState.update { it.copy(accountEmail = email) }
    }

    fun onApartamentoClick(apartamentoId: String) {
        _uiState.update {
            it.copy(navigationEvent = MoradorInfoNavigationEvent.ApartamentoDetails(apartamentoId))
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }

    fun setAdminMode(isAdmin: Boolean) {
        _uiState.update { it.copy(isAdminMode = isAdmin) }
    }

    fun onSaveTipo(apartamentoId: String, newTipo: MoradorTipo) {
        if (pessoaId.isBlank()) return

        viewModelScope.launch {
            updateMoradorTipoUseCase(pessoaId, apartamentoId, newTipo)
                .onSuccess {
                    _uiState.update { it.copy(tipoError = null) }
                    load()
                }
                .onFailure { error ->
                    if (error is MoradorException.MaxProprietariosExceededException) {
                        _uiState.update { it.copy(tipoError = "Limite de 2 proprietarios por apartamento atingido") }
                    }
                }
        }
    }

    // Account creation methods

    fun onShowCreateAccountDialog() {
        val currentEmail = _uiState.value.email
        val prefillEmail = if (currentEmail != "Nao informado") currentEmail else ""
        _uiState.update {
            it.copy(
                showCreateAccountDialog = true,
                createAccountEmail = prefillEmail,
                createAccountPassword = "",
                createAccountEmailError = null,
                createAccountPasswordError = null,
                createAccountError = null,
                accountCreatedSuccess = false
            )
        }
    }

    fun onDismissCreateAccountDialog() {
        _uiState.update {
            it.copy(
                showCreateAccountDialog = false,
                createAccountEmail = "",
                createAccountPassword = "",
                createAccountEmailError = null,
                createAccountPasswordError = null,
                createAccountError = null,
                accountCreatedSuccess = false
            )
        }
    }

    fun onCreateAccountEmailChanged(email: String) {
        _uiState.update { it.copy(createAccountEmail = email, createAccountEmailError = null, createAccountError = null) }
    }

    fun onCreateAccountPasswordChanged(password: String) {
        _uiState.update { it.copy(createAccountPassword = password, createAccountPasswordError = null, createAccountError = null) }
    }

    fun onCreateAccount() {
        val state = _uiState.value
        if (pessoaId.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isCreatingAccount = true,
                    createAccountEmailError = null,
                    createAccountPasswordError = null,
                    createAccountError = null
                )
            }

            moradorAccountCreator.createAccount(
                pessoaId = pessoaId,
                email = state.createAccountEmail,
                password = state.createAccountPassword
            )
                .onSuccess { email ->
                    _uiState.update {
                        it.copy(
                            isCreatingAccount = false,
                            accountCreatedSuccess = true,
                            accountEmail = email,
                            showCreateAccountDialog = false
                        )
                    }
                }
                .onFailure { error ->
                    val message = error.message ?: "Erro ao criar conta"
                    // Map known error messages to specific fields
                    when {
                        message.contains("Email") && (message.contains("obrigat") || message.contains("conter @")) ->
                            _uiState.update { it.copy(isCreatingAccount = false, createAccountEmailError = message) }
                        message.contains("email") && message.contains("uso") ->
                            _uiState.update { it.copy(isCreatingAccount = false, createAccountEmailError = message) }
                        message.contains("Senha") ->
                            _uiState.update { it.copy(isCreatingAccount = false, createAccountPasswordError = message) }
                        else ->
                            _uiState.update { it.copy(isCreatingAccount = false, createAccountError = message) }
                    }
                }
        }
    }
}
