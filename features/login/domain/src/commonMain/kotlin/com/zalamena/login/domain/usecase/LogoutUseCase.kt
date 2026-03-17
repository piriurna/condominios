package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.repository.LoginRepository

class LogoutUseCase(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke() {
        loginRepository.logout()
    }
}
