package com.zalamena.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalamena.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SplashNavigationEvent {
    object NavigateToLogin : SplashNavigationEvent()
    object NavigateToHome : SplashNavigationEvent()
}

class SplashViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _navEvent = MutableStateFlow<SplashNavigationEvent?>(null)
    val navEvent: StateFlow<SplashNavigationEvent?> = _navEvent

    init {
        viewModelScope.launch {
            _navEvent.value = if (loginRepository.isLoggedIn())
                SplashNavigationEvent.NavigateToHome
            else
                SplashNavigationEvent.NavigateToLogin
        }
    }

    fun onNavigationHandled() {
        _navEvent.value = null
    }
}
