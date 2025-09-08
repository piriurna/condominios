package com.zalamena.login.domain.usecase

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
        val token = "token"


        everySuspending { loginRepository.login(username, password) } returns token


        val result = loginUseCase(username, password)


        assertTrue(result.isSuccess)
        assertEquals(result.getOrThrow(), token)
    }


    @Test
    fun `GIVEN a username is not registered WHEN trying to log in THEN it should say it is not registered`() = runTest {
        val username = "username"
        val password = "password"

        everySuspending { loginRepository.login(username, password) } runs {
            throw LoginException.NonExistentUserException
        }

        val result = loginUseCase(username, password)

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals("Non existent user", it.message)
        }
    }

    @Test
    fun `GIVEN a username is registered but password is wrong WHEN trying to log in THEN it should say password is incorrect`() = runTest {
        val username = "username"
        val password = "password"

        everySuspending { loginRepository.login(username, password) } runs {
            throw LoginException.InvalidCredentialsException
        }

        val result = loginUseCase(username, password)

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals("Invalid credentials", it.message)
        }
    }

    @Test
    fun `GIVEN a username and password is correct tries to log in WHEN there is a network problem THEN it should fail the login`() =
        runTest {
            val username = "username"
            val password = "password"

            everySuspending { loginRepository.login(username, password) } runs {
                throw LoginException.GenericErrorException("No internet connection")
            }

            val result = loginUseCase(username, password)

            assertTrue(result.isFailure)
            result.onFailure {
                assertEquals("No internet connection", it.message)
            }
        }

}