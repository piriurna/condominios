package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.CondominioValidator
import com.zalamena.login.domain.repository.UserRepository

sealed class CreateUserError(override val message: String) : Exception(message) {
    object EmptyName : CreateUserError("Nome é obrigatório")
    object EmptyCpf : CreateUserError("CPF é obrigatório")
    object EmptyEmail : CreateUserError("Email é obrigatório")
    object CondominioNotFound : CreateUserError("Condomínio não encontrado")
}

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val condominioValidator: CondominioValidator
) {

    suspend operator fun invoke(
        name: String,
        cpf: String,
        email: String,
        role: UserRole,
        password: String
    ): Result<User> {
        if (name.isBlank()) return Result.failure(CreateUserError.EmptyName)
        if (cpf.isBlank()) return Result.failure(CreateUserError.EmptyCpf)
        if (email.isBlank()) return Result.failure(CreateUserError.EmptyEmail)

        if (role is UserRole.Porteiro && !condominioValidator.exists(role.condominioId)) {
            return Result.failure(CreateUserError.CondominioNotFound)
        }

        return userRepository.createUser(
            name = name.trim(),
            cpf = cpf.trim(),
            email = email.trim(),
            role = role,
            password = password
        )
    }
}
