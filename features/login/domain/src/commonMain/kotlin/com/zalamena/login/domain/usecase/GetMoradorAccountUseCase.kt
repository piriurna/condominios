package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.repository.UserRepository

class GetMoradorAccountUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(pessoaId: String): Result<User?> =
        userRepository.getUserByPessoaId(pessoaId)
}
