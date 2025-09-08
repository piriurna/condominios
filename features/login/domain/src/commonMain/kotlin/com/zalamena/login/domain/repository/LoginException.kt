package com.zalamena.login.domain.repository

sealed class LoginException(override val message: String): Exception(message) {

    object InvalidCredentialsException: LoginException("Invalid credentials")

    object NonExistentUserException: LoginException("Non existent user")

    class GenericErrorException(message: String): LoginException(message)
}