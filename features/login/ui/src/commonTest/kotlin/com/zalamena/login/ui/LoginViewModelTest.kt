package com.zalamena.login.ui

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.LoginException
import com.zalamena.login.domain.repository.LoginRepository
import com.zalamena.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var loginRepository: LoginRepository

    private val loginUseCase by lazy { LoginUseCase(loginRepository) }
    private val viewModel by lazy { LoginViewModel(loginUseCase) }

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state THEN fields are empty, no loading, no error, no nav event`() {
        val state = viewModel.uiState.value
        assertEquals("", state.username)
        assertEquals("", state.password)
        assertTrue(!state.isLoading)
        assertNull(state.error)
        assertNull(state.usernameError)
        assertNull(state.passwordError)
        assertNull(state.navigationEvent)
    }

    @Test
    fun `GIVEN setUsername called THEN username is updated in state`() {
        viewModel.setUsername("admin")
        assertEquals("admin", viewModel.uiState.value.username)
    }

    @Test
    fun `GIVEN setPassword called THEN password is updated in state`() {
        viewModel.setPassword("secret")
        assertEquals("secret", viewModel.uiState.value.password)
    }

    @Test
    fun `GIVEN admin login succeeds THEN navigationEvent is NavigateToAdminHome and isLoading is false`() = runTest {
        val user = User(id = "user-1", name = "Admin", cpf = "00000000000", email = "admin@test.com", role = UserRole.Admin)
        everySuspending { loginRepository.login(isAny(), isAny()) } returns Result.success(user)

        viewModel.setUsername("admin")
        viewModel.setPassword("admin")
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(LoginNavigationEvent.NavigateToAdminHome, state.navigationEvent)
        assertTrue(!state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `GIVEN porteiro login succeeds THEN navigationEvent is NavigateToDoormanHome with condominioId`() = runTest {
        val user = User(id = "user-2", name = "Porteiro", cpf = "11111111111", email = "porteiro@test.com", role = UserRole.Porteiro("condo-1"))
        everySuspending { loginRepository.login(isAny(), isAny()) } returns Result.success(user)

        viewModel.setUsername("porteiro")
        viewModel.setPassword("porteiro")
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val navEvent = state.navigationEvent
        assertIs<LoginNavigationEvent.NavigateToDoormanHome>(navEvent)
        assertEquals("condo-1", navEvent.condominioId)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `GIVEN login fails with invalid credentials THEN error is set and no nav event`() = runTest {
        everySuspending { loginRepository.login(isAny(), isAny()) } returns
            Result.failure(LoginException.InvalidCredentialsException)

        viewModel.setUsername("wrong")
        viewModel.setPassword("wrong")
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.navigationEvent)
        assertTrue(!state.isLoading)
        assertEquals(LoginException.InvalidCredentialsException.message, state.error)
    }

    @Test
    fun `GIVEN login fails with non existent user THEN error message matches exception`() = runTest {
        everySuspending { loginRepository.login(isAny(), isAny()) } returns
            Result.failure(LoginException.NonExistentUserException)

        viewModel.setUsername("someone")
        viewModel.setPassword("pass")
        viewModel.login()
        advanceUntilIdle()

        assertEquals(LoginException.NonExistentUserException.message, viewModel.uiState.value.error)
    }

    @Test
    fun `GIVEN onNavigationHandled called after success THEN navigationEvent is cleared`() = runTest {
        val user = User(id = "user-1", name = "Admin", cpf = "00000000000", email = "admin@test.com", role = UserRole.Admin)
        everySuspending { loginRepository.login(isAny(), isAny()) } returns Result.success(user)

        viewModel.setUsername("admin")
        viewModel.setPassword("admin")
        viewModel.login()
        advanceUntilIdle()

        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN empty username WHEN login THEN usernameError is set and no API call`() = runTest {
        viewModel.setPassword("admin")
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Campo obrigatório", state.usernameError)
        assertNull(state.navigationEvent)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `GIVEN empty password WHEN login THEN passwordError is set and no API call`() = runTest {
        viewModel.setUsername("admin")
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Campo obrigatório", state.passwordError)
        assertNull(state.navigationEvent)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `GIVEN both empty WHEN login THEN both errors are set`() = runTest {
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Campo obrigatório", state.usernameError)
        assertEquals("Campo obrigatório", state.passwordError)
        assertNull(state.navigationEvent)
    }

    @Test
    fun `GIVEN usernameError exists WHEN setUsername THEN error is cleared`() {
        // Trigger error first
        viewModel.login()
        assertEquals("Campo obrigatório", viewModel.uiState.value.usernameError)

        // Now set username should clear the error
        viewModel.setUsername("admin")
        assertNull(viewModel.uiState.value.usernameError)
    }

    @Test
    fun `GIVEN passwordError exists WHEN setPassword THEN error is cleared`() {
        // Trigger error first
        viewModel.login()
        assertEquals("Campo obrigatório", viewModel.uiState.value.passwordError)

        // Now set password should clear the error
        viewModel.setPassword("admin")
        assertNull(viewModel.uiState.value.passwordError)
    }
}
