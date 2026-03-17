package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.CondominioValidator
import com.zalamena.login.domain.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CreateUserUseCaseTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var condominioValidator: CondominioValidator

    private val useCase by lazy { CreateUserUseCase(userRepository, condominioValidator) }

    @Test
    fun `GIVEN valid admin data WHEN creating user THEN returns success`() = runTest {
        val role = UserRole.Admin
        val user = User(id = "user-1", name = "Admin", cpf = "12345678900", email = "admin@test.com", role = role)
        everySuspending { userRepository.createUser("Admin", "12345678900", "admin@test.com", role, "pass123") } returns Result.success(user)

        val result = useCase("Admin", "12345678900", "admin@test.com", role, "pass123")

        assertTrue(result.isSuccess)
        assertEquals("Admin", result.getOrThrow().name)
        assertIs<UserRole.Admin>(result.getOrThrow().role)
    }

    @Test
    fun `GIVEN valid porteiro data WHEN creating user THEN returns success`() = runTest {
        val role = UserRole.Porteiro("condo-1")
        val user = User(id = "user-2", name = "Porteiro", cpf = "11111111111", email = "porteiro@test.com", role = role)
        everySuspending { condominioValidator.exists("condo-1") } returns true
        everySuspending { userRepository.createUser("Porteiro", "11111111111", "porteiro@test.com", role, "pass123") } returns Result.success(user)

        val result = useCase("Porteiro", "11111111111", "porteiro@test.com", role, "pass123")

        assertTrue(result.isSuccess)
        val resultRole = result.getOrThrow().role
        assertIs<UserRole.Porteiro>(resultRole)
        assertEquals("condo-1", resultRole.condominioId)
    }

    @Test
    fun `GIVEN porteiro with nonexistent condominioId WHEN creating user THEN returns CondominioNotFound error`() = runTest {
        val role = UserRole.Porteiro("nonexistent-condo")
        everySuspending { condominioValidator.exists("nonexistent-condo") } returns false

        val result = useCase("Porteiro", "11111111111", "porteiro@test.com", role, "pass123")

        assertTrue(result.isFailure)
        assertIs<CreateUserError.CondominioNotFound>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN empty name WHEN creating user THEN returns EmptyName error`() = runTest {
        val result = useCase("", "12345678900", "admin@test.com", UserRole.Admin, "pass123")

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyName>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN blank name WHEN creating user THEN returns EmptyName error`() = runTest {
        val result = useCase("   ", "12345678900", "admin@test.com", UserRole.Admin, "pass123")

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyName>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN empty cpf WHEN creating user THEN returns EmptyCpf error`() = runTest {
        val result = useCase("Admin", "", "admin@test.com", UserRole.Admin, "pass123")

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyCpf>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN empty email WHEN creating user THEN returns EmptyEmail error`() = runTest {
        val result = useCase("Admin", "12345678900", "", UserRole.Admin, "pass123")

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyEmail>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN name with whitespace WHEN creating user THEN name is trimmed`() = runTest {
        val role = UserRole.Admin
        val user = User(id = "user-3", name = "Admin", cpf = "12345678900", email = "admin@test.com", role = role)
        everySuspending { userRepository.createUser("Admin", "12345678900", "admin@test.com", role, "pass123") } returns Result.success(user)

        val result = useCase("  Admin  ", "  12345678900  ", "  admin@test.com  ", role, "pass123")

        assertTrue(result.isSuccess)
    }
}
