package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.MoradorValidator
import com.zalamena.login.domain.repository.UserRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CreateMoradorAccountUseCaseTest {

    private val users = mutableListOf<User>()

    private val fakeUserRepository = object : UserRepository {
        override suspend fun createUser(
            name: String,
            cpf: String,
            email: String,
            role: UserRole,
            password: String
        ): Result<User> {
            val user = User(id = "new-id", name = name, cpf = cpf, email = email, role = role)
            users.add(user)
            return Result.success(user)
        }

        override suspend fun getUsers(): Result<List<User>> = Result.success(users.toList())
        override suspend fun getUsersByCondominioId(condominioId: String): Result<List<User>> =
            Result.success(users.filter { (it.role as? UserRole.Porteiro)?.condominioId == condominioId })
        override suspend fun deleteUser(userId: String): Result<Unit> = Result.success(Unit)
        override suspend fun updateUserRole(userId: String, newRole: UserRole): Result<Unit> = Result.success(Unit)
        override suspend fun authenticate(email: String, password: String): Result<User> =
            Result.failure(NoSuchElementException())
        override suspend fun getUserByPessoaId(pessoaId: String): Result<User?> =
            Result.success(users.find { (it.role as? UserRole.Morador)?.pessoaId == pessoaId })
    }

    private val validMoradorValidator = MoradorValidator { "condo-1" }

    private val invalidMoradorValidator = MoradorValidator { null }

    private fun createUseCase(
        moradorValidator: MoradorValidator = validMoradorValidator
    ) = CreateMoradorAccountUseCase(fakeUserRepository, moradorValidator)

    @Test
    fun `returns EmptyEmail when email is blank`() = runTest {
        val result = createUseCase()("pessoa-1", "", "password123")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.EmptyEmail>(result.exceptionOrNull())
    }

    @Test
    fun `returns InvalidEmail when email has no at sign`() = runTest {
        val result = createUseCase()("pessoa-1", "bademail", "password123")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.InvalidEmail>(result.exceptionOrNull())
    }

    @Test
    fun `returns EmptyPassword when password is blank`() = runTest {
        val result = createUseCase()("pessoa-1", "morador@test.com", "  ")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.EmptyPassword>(result.exceptionOrNull())
    }

    @Test
    fun `returns MoradorNotFound when pessoa is not a morador`() = runTest {
        val result = createUseCase(invalidMoradorValidator)("pessoa-1", "morador@test.com", "password123")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.MoradorNotFound>(result.exceptionOrNull())
    }

    @Test
    fun `returns EmailAlreadyTaken when email is in use`() = runTest {
        users.add(
            User(
                id = "existing",
                name = "Existing",
                cpf = "00000000000",
                email = "taken@test.com",
                role = UserRole.Admin
            )
        )

        val result = createUseCase()("pessoa-1", "taken@test.com", "password123")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.EmailAlreadyTaken>(result.exceptionOrNull())
    }

    @Test
    fun `returns AccountAlreadyExists when morador already has account`() = runTest {
        users.add(
            User(
                id = "existing",
                name = "",
                cpf = "",
                email = "existing@test.com",
                role = UserRole.Morador(pessoaId = "pessoa-1", condominioId = "condo-1")
            )
        )

        val result = createUseCase()("pessoa-1", "new@test.com", "password123")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.AccountAlreadyExists>(result.exceptionOrNull())
    }

    @Test
    fun `creates user with Morador role on success`() = runTest {
        val result = createUseCase()("pessoa-1", "morador@test.com", "password123")
        assertTrue(result.isSuccess)

        val user = result.getOrThrow()
        assertIs<UserRole.Morador>(user.role)
        assertEquals("pessoa-1", (user.role as UserRole.Morador).pessoaId)
        assertEquals("condo-1", (user.role as UserRole.Morador).condominioId)
        assertEquals("morador@test.com", user.email)
    }

    @Test
    fun `trims email before creating user`() = runTest {
        val result = createUseCase()("pessoa-1", "  morador@test.com  ", "password123")
        assertTrue(result.isSuccess)
        assertEquals("morador@test.com", result.getOrThrow().email)
    }

    @Test
    fun `email uniqueness check is case insensitive`() = runTest {
        users.add(
            User(
                id = "existing",
                name = "Existing",
                cpf = "00000000000",
                email = "Taken@Test.com",
                role = UserRole.Admin
            )
        )

        val result = createUseCase()("pessoa-1", "taken@test.com", "password123")
        assertTrue(result.isFailure)
        assertIs<CreateMoradorAccountError.EmailAlreadyTaken>(result.exceptionOrNull())
    }
}
