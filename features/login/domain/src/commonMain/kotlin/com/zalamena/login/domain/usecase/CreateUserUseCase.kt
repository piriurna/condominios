package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.UserRepository

sealed class CreateUserError(override val message: String) : Exception(message) {
    object EmptyName : CreateUserError("Nome é obrigatório")
    object EmptyCpf : CreateUserError("CPF é obrigatório")
    object EmptyEmail : CreateUserError("Email é obrigatório")
    object PorteiroRequiresCondominio : CreateUserError("Porteiro deve estar associado a um condomínio")
}

class CreateUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        cpf: String,
        email: String,
        role: UserRole,
        condominioId: String?
    ): Result<User> {
        if (name.isBlank()) return Result.failure(CreateUserError.EmptyName)
        if (cpf.isBlank()) return Result.failure(CreateUserError.EmptyCpf)
        if (email.isBlank()) return Result.failure(CreateUserError.EmptyEmail)
        if (role == UserRole.PORTEIRO && condominioId.isNullOrBlank()) {
            return Result.failure(CreateUserError.PorteiroRequiresCondominio)
        }

        return userRepository.createUser(
            name = name.trim(),
            cpf = cpf.trim(),
            email = email.trim(),
            role = role,
            condominioId = condominioId?.trim()?.takeIf { it.isNotBlank() }
        )
    }
}
