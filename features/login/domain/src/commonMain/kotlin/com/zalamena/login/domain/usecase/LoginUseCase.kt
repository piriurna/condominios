package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.repository.LoginRepository

class LoginUseCase(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke(username: String, password: String): Result<String> {

        return runCatching {
            val loginResult = loginRepository.login(username, password)


            loginResult
        }
        .onFailure { it ->
            it.message
        }
    }
}