package com.zalamena.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val navigationEvent: LoginNavigationEvent? = null
)

sealed class LoginNavigationEvent {
    object NavigateToAdminHome : LoginNavigationEvent()
    data class NavigateToDoormanHome(val condominioId: String) : LoginNavigationEvent()
    data class NavigateToMoradorHome(val condominioId: String) : LoginNavigationEvent()
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun setUsername(username: String) {
        _uiState.update { it.copy(username = username, usernameError = null) }
    }

    fun setPassword(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun login() {
        val currentState = _uiState.value
        val usernameError = if (currentState.username.isEmpty()) "Campo obrigatório" else null
        val passwordError = if (currentState.password.isEmpty()) "Campo obrigatório" else null

        if (usernameError != null || passwordError != null) {
            _uiState.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUseCase(_uiState.value.username, _uiState.value.password)
                .onSuccess { user ->
                    val navEvent = when (val role = user.role) {
                        is UserRole.Admin -> LoginNavigationEvent.NavigateToAdminHome
                        is UserRole.Porteiro -> LoginNavigationEvent.NavigateToDoormanHome(role.condominioId)
                        is UserRole.Morador -> LoginNavigationEvent.NavigateToMoradorHome(role.condominioId)
                    }
                    _uiState.update {
                        it.copy(isLoading = false, navigationEvent = navEvent)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Erro ao fazer login")
                    }
                }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }
}
