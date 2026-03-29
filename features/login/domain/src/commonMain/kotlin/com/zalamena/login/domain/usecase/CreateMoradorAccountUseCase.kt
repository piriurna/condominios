package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.MoradorValidator
import com.zalamena.login.domain.repository.UserRepository

sealed class CreateMoradorAccountError(override val message: String) : Exception(message) {
    object EmptyEmail : CreateMoradorAccountError("Email é obrigatório")
    object InvalidEmail : CreateMoradorAccountError("Email deve conter @")
    object EmptyPassword : CreateMoradorAccountError("Senha é obrigatória")
    object EmailAlreadyTaken : CreateMoradorAccountError("Este email já está em uso")
    object MoradorNotFound : CreateMoradorAccountError("Pessoa não é um morador em nenhum apartamento")
    object AccountAlreadyExists : CreateMoradorAccountError("Este morador já possui uma conta")
}

class CreateMoradorAccountUseCase(
    private val userRepository: UserRepository,
    private val moradorValidator: MoradorValidator
) {

    suspend operator fun invoke(
        pessoaId: String,
        email: String,
        password: String
    ): Result<User> {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank()) return Result.failure(CreateMoradorAccountError.EmptyEmail)
        if (!trimmedEmail.contains("@")) return Result.failure(CreateMoradorAccountError.InvalidEmail)
        if (trimmedPassword.isBlank()) return Result.failure(CreateMoradorAccountError.EmptyPassword)

        // Check if this morador already has an account
        val existingUsers = userRepository.getUsers().getOrElse { emptyList() }
        val alreadyHasAccount = existingUsers.any { user ->
            val role = user.role
            role is UserRole.Morador && role.pessoaId == pessoaId
        }
        if (alreadyHasAccount) return Result.failure(CreateMoradorAccountError.AccountAlreadyExists)

        // Check email uniqueness
        val emailTaken = existingUsers.any { it.email.equals(trimmedEmail, ignoreCase = true) }
        if (emailTaken) return Result.failure(CreateMoradorAccountError.EmailAlreadyTaken)

        // Validate the pessoa is a morador and get their condominioId
        val condominioId = moradorValidator.getCondominioIdForMorador(pessoaId)
            ?: return Result.failure(CreateMoradorAccountError.MoradorNotFound)

        return userRepository.createUser(
            name = "", // Name comes from Pessoa, not needed for account creation
            cpf = "",  // CPF comes from Pessoa, not needed for account creation
            email = trimmedEmail,
            role = UserRole.Morador(pessoaId = pessoaId, condominioId = condominioId),
            password = trimmedPassword
        )
    }
}
