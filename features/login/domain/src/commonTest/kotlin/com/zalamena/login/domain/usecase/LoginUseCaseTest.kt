package com.zalamena.login.domain.usecase

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.repository.LoginException
import com.zalamena.login.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.tests.TestsWithMocks
import org.kodein.mock.generated.injectMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest: TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var loginRepository: LoginRepository

    val loginUseCase by lazy { LoginUseCase(loginRepository) }

    @Test
    fun `GIVEN a username and password logs in WHEN it exists and password matches THEN it should be a successful login`() = runTest {
        val username = "username"
        val password = "password"
        val cpf = "cpf"
        val email = "email"

        everySuspending { loginRepository.login(username, password) } returns Result.success(User(
            name = username,
            cpf = cpf,
            email = email
        ))


        val result = loginUseCase(username, password)


        assertTrue(result.isSuccess)
    }


    @Test
    fun `GIVEN a username is not registered WHEN trying to log in THEN it should say it is not registered`() = runTest {
        val username = "username"
        val password = "password"

        everySuspending { loginRepository.login(username, password) } returns Result.failure(
            LoginException.NonExistentUserException
        )

        val result = loginUseCase(username, password)

        assertEquals(LoginException.NonExistentUserException, result.exceptionOrNull())
    }

    @Test
    fun `GIVEN a username is registered but password is wrong WHEN trying to log in THEN it should say password is incorrect`() = runTest {
        val username = "username"
        val password = "password"

        everySuspending { loginRepository.login(username, password) } returns
                Result.failure(LoginException.InvalidCredentialsException)

        val result = loginUseCase(username, password)

        assertTrue(result.isFailure)
        assertEquals(LoginException.InvalidCredentialsException, result.exceptionOrNull())
    }

    @Test
    fun `GIVEN a username and password is correct tries to log in WHEN there is a network problem THEN it should fail the login`() =
        runTest {
            val username = "username"
            val password = "password"

            everySuspending { loginRepository.login(username, password) } returns Result.failure(
                LoginException.GenericErrorException("No internet connection")
            )

            val result = loginUseCase(username, password)

            assertTrue(result.isFailure)

            assertEquals("No internet connection", result.exceptionOrNull()?.message)
        }

}