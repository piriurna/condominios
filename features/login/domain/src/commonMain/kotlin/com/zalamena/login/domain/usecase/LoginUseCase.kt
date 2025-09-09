package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.repository.LoginRepository

class LoginUseCase(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke(username: String, password: String): Result<User> {
        val loginResult = loginRepository.login(username, password)


        return loginResult
    }
}