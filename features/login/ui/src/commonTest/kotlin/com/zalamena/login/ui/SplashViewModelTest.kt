package com.zalamena.login.ui

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

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var loginRepository: LoginRepository

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
    fun `GIVEN user is logged in WHEN splash starts THEN NavigateToHome is emitted`() = runTest {
        everySuspending { loginRepository.isLoggedIn() } returns true

        val viewModel = SplashViewModel(loginRepository)
        advanceUntilIdle()

        assertEquals(SplashNavigationEvent.NavigateToHome, viewModel.navEvent.value)
    }

    @Test
    fun `GIVEN user is not logged in WHEN splash starts THEN NavigateToLogin is emitted`() = runTest {
        everySuspending { loginRepository.isLoggedIn() } returns false

        val viewModel = SplashViewModel(loginRepository)
        advanceUntilIdle()

        assertEquals(SplashNavigationEvent.NavigateToLogin, viewModel.navEvent.value)
    }

    @Test
    fun `GIVEN navEvent emitted WHEN onNavigationHandled called THEN navEvent is cleared`() = runTest {
        everySuspending { loginRepository.isLoggedIn() } returns false

        val viewModel = SplashViewModel(loginRepository)
        advanceUntilIdle()

        viewModel.onNavigationHandled()

        assertNull(viewModel.navEvent.value)
    }
}
