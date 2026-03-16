package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
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

    private val useCase by lazy { CreateUserUseCase(userRepository) }

    @Test
    fun `GIVEN valid admin data WHEN creating user THEN returns success`() = runTest {
        val user = User(name = "Admin", cpf = "12345678900", email = "admin@test.com", role = UserRole.ADMIN)
        everySuspending { userRepository.createUser("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null) } returns Result.success(user)

        val result = useCase("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null)

        assertTrue(result.isSuccess)
        assertEquals("Admin", result.getOrThrow().name)
        assertEquals(UserRole.ADMIN, result.getOrThrow().role)
    }

    @Test
    fun `GIVEN valid porteiro data with condominioId WHEN creating user THEN returns success`() = runTest {
        val user = User(name = "Porteiro", cpf = "11111111111", email = "porteiro@test.com", role = UserRole.PORTEIRO, condominioId = "condo-1")
        everySuspending { userRepository.createUser("Porteiro", "11111111111", "porteiro@test.com", UserRole.PORTEIRO, "condo-1") } returns Result.success(user)

        val result = useCase("Porteiro", "11111111111", "porteiro@test.com", UserRole.PORTEIRO, "condo-1")

        assertTrue(result.isSuccess)
        assertEquals(UserRole.PORTEIRO, result.getOrThrow().role)
        assertEquals("condo-1", result.getOrThrow().condominioId)
    }

    @Test
    fun `GIVEN empty name WHEN creating user THEN returns EmptyName error`() = runTest {
        val result = useCase("", "12345678900", "admin@test.com", UserRole.ADMIN, null)

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyName>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN blank name WHEN creating user THEN returns EmptyName error`() = runTest {
        val result = useCase("   ", "12345678900", "admin@test.com", UserRole.ADMIN, null)

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyName>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN empty cpf WHEN creating user THEN returns EmptyCpf error`() = runTest {
        val result = useCase("Admin", "", "admin@test.com", UserRole.ADMIN, null)

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyCpf>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN empty email WHEN creating user THEN returns EmptyEmail error`() = runTest {
        val result = useCase("Admin", "12345678900", "", UserRole.ADMIN, null)

        assertTrue(result.isFailure)
        assertIs<CreateUserError.EmptyEmail>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN porteiro without condominioId WHEN creating user THEN returns PorteiroRequiresCondominio error`() = runTest {
        val result = useCase("Porteiro", "11111111111", "porteiro@test.com", UserRole.PORTEIRO, null)

        assertTrue(result.isFailure)
        assertIs<CreateUserError.PorteiroRequiresCondominio>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN porteiro with blank condominioId WHEN creating user THEN returns PorteiroRequiresCondominio error`() = runTest {
        val result = useCase("Porteiro", "11111111111", "porteiro@test.com", UserRole.PORTEIRO, "   ")

        assertTrue(result.isFailure)
        assertIs<CreateUserError.PorteiroRequiresCondominio>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN name with whitespace WHEN creating user THEN name is trimmed`() = runTest {
        val user = User(name = "Admin", cpf = "12345678900", email = "admin@test.com", role = UserRole.ADMIN)
        everySuspending { userRepository.createUser("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null) } returns Result.success(user)

        val result = useCase("  Admin  ", "  12345678900  ", "  admin@test.com  ", UserRole.ADMIN, null)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `GIVEN admin with condominioId WHEN creating user THEN condominioId is passed through`() = runTest {
        val user = User(name = "Admin", cpf = "12345678900", email = "admin@test.com", role = UserRole.ADMIN, condominioId = null)
        everySuspending { userRepository.createUser("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null) } returns Result.success(user)

        val result = useCase("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null)

        assertTrue(result.isSuccess)
    }
}
