package com.zalamena.login.ui

import com.zalamena.login.domain.models.User
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
    fun `GIVEN login succeeds THEN navigationEvent is LoginSuccess and isLoading is false`() = runTest {
        val user = User(name = "Admin", cpf = "00000000000", email = "admin@test.com")
        everySuspending { loginRepository.login(isAny(), isAny()) } returns Result.success(user)

        viewModel.setUsername("admin")
        viewModel.setPassword("admin")
        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(LoginNavigationEvent.LoginSuccess, state.navigationEvent)
        assertTrue(!state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `GIVEN login fails with invalid credentials THEN error is set and no nav event`() = runTest {
        everySuspending { loginRepository.login(isAny(), isAny()) } returns
            Result.failure(LoginException.InvalidCredentialsException)

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

        viewModel.login()
        advanceUntilIdle()

        assertEquals(LoginException.NonExistentUserException.message, viewModel.uiState.value.error)
    }

    @Test
    fun `GIVEN onNavigationHandled called after success THEN navigationEvent is cleared`() = runTest {
        val user = User(name = "Admin", cpf = "00000000000", email = "admin@test.com")
        everySuspending { loginRepository.login(isAny(), isAny()) } returns Result.success(user)

        viewModel.login()
        advanceUntilIdle()

        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }
}
