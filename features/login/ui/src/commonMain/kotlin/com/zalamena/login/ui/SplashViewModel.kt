package com.zalamena.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SplashNavigationEvent {
    object NavigateToLogin : SplashNavigationEvent()
    object NavigateToAdminHome : SplashNavigationEvent()
    data class NavigateToDoormanHome(val condominioId: String) : SplashNavigationEvent()
    data class NavigateToMoradorHome(val condominioId: String) : SplashNavigationEvent()
}

class SplashViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _navEvent = MutableStateFlow<SplashNavigationEvent?>(null)
    val navEvent: StateFlow<SplashNavigationEvent?> = _navEvent

    init {
        viewModelScope.launch {
            if (loginRepository.isLoggedIn()) {
                val role = loginRepository.getRole()
                _navEvent.value = when (role) {
                    is UserRole.Porteiro -> SplashNavigationEvent.NavigateToDoormanHome(role.condominioId)
                    is UserRole.Morador -> SplashNavigationEvent.NavigateToMoradorHome(role.condominioId)
                    is UserRole.Admin -> SplashNavigationEvent.NavigateToAdminHome
                    null -> SplashNavigationEvent.NavigateToLogin
                }
            } else {
                _navEvent.value = SplashNavigationEvent.NavigateToLogin
            }
        }
    }

    fun onNavigationHandled() {
        _navEvent.value = null
    }
}
