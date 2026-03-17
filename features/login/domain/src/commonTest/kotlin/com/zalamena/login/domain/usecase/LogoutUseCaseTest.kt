package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test

class LogoutUseCaseTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var loginRepository: LoginRepository

    private val logoutUseCase by lazy { LogoutUseCase(loginRepository) }

    @Test
    fun `GIVEN logged in user WHEN logout is called THEN repository logout is invoked`() = runTest {
        everySuspending { loginRepository.logout() } returns Unit

        logoutUseCase()

        verifyWithSuspend { loginRepository.logout() }
    }
}
