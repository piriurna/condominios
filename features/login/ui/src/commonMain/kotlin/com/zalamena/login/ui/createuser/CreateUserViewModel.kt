package com.zalamena.login.ui.createuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.usecase.CreateUserError
import com.zalamena.login.domain.usecase.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class RoleOption { ADMIN, PORTEIRO }

data class CreateUserUiState(
    val name: String = "",
    val cpf: String = "",
    val email: String = "",
    val selectedRole: RoleOption = RoleOption.PORTEIRO,
    val condominioId: String = "",
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val cpfError: String? = null,
    val emailError: String? = null,
    val condominioIdError: String? = null,
    val error: String? = null,
    val generatedPassword: String? = null,
    val navigationEvent: CreateUserNavigationEvent? = null
)

sealed class CreateUserNavigationEvent {
    data object NavigateBack : CreateUserNavigationEvent()
}

class CreateUserViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUserUiState())
    val uiState: StateFlow<CreateUserUiState> = _uiState

    fun setName(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun setCpf(cpf: String) {
        _uiState.update { it.copy(cpf = cpf, cpfError = null) }
    }

    fun setEmail(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun setRole(role: RoleOption) {
        _uiState.update { it.copy(selectedRole = role, condominioIdError = null) }
    }

    fun setCondominioId(condominioId: String) {
        _uiState.update { it.copy(condominioId = condominioId, condominioIdError = null) }
    }

    fun createUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, generatedPassword = null) }

            val state = _uiState.value

            val role = when (state.selectedRole) {
                RoleOption.ADMIN -> UserRole.Admin
                RoleOption.PORTEIRO -> {
                    val condoId = state.condominioId.trim()
                    if (condoId.isBlank()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                condominioIdError = "Porteiro deve estar associado a um condomínio"
                            )
                        }
                        return@launch
                    }
                    UserRole.Porteiro(condoId)
                }
            }

            val password = generatePassword()

            val result = createUserUseCase(
                name = state.name,
                cpf = state.cpf,
                email = state.email,
                role = role,
                password = password
            )

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generatedPassword = password
                        )
                    }
                }
                .onFailure { error ->
                    when (error) {
                        is CreateUserError.EmptyName ->
                            _uiState.update { it.copy(isLoading = false, nameError = error.message) }
                        is CreateUserError.EmptyCpf ->
                            _uiState.update { it.copy(isLoading = false, cpfError = error.message) }
                        is CreateUserError.EmptyEmail ->
                            _uiState.update { it.copy(isLoading = false, emailError = error.message) }
                        else ->
                            _uiState.update { it.copy(isLoading = false, error = error.message ?: "Erro ao criar usuário") }
                    }
                }
        }
    }

    fun reset() {
        _uiState.value = CreateUserUiState()
    }

    fun onNavigateBack() {
        _uiState.update { it.copy(navigationEvent = CreateUserNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }

    companion object {
        fun generatePassword(): String {
            val chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789"
            return (1..8).map { chars.random() }.joinToString("")
        }
    }
}
