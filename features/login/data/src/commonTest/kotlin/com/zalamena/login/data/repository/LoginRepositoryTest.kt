package com.zalamena.login.data.repository

import com.zalamena.login.data.api.LoginApi
import com.zalamena.login.data.models.LoginSessionDto
import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.LoginException
import com.zalamena.login.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginRepositoryTest: TestsWithMocks() {

    @Mock
    lateinit var loginApi: LoginApi

    @Mock
    lateinit var sessionRepository: SessionRepository

    private val loginRepository: LoginRepository by lazy { LoginRepositoryImpl(loginApi, sessionRepository) }


    @Test
    fun `GIVEN a correct username and password WHEN logging in THEN it should return the logged user`() = runTest {
        val username = "username"
        val password = "password"

        mockSuccessfulApiLogin()
        justRunsSaveSession()
        mockSuccessGetUserApiCall()


        val result = loginRepository.login(username, password)


        assertEquals("logged user name",result.getOrThrow().name)
    }

    @Test
    fun `GIVEN an incorrect username and password WHEN logging in THEN it should be a failed login with Login Failed error message`() = runTest {
        val username = "username"
        val password = "incorrectPassword"


        val result = loginRepository.login(username, password)


        assertEquals(
            "Login Failed",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `GIVEN a successful login is made WHEN a session is returned THEN should save the session`() = runTest {
        val sessionToken = "sessionToken"
        val expiresIn = 10L
        val username = "username"
        val password = "password"


        mockSuccessfulApiLogin()
        justRunsSaveSession()
        mockSuccessGetUserApiCall()


        loginRepository.login(username, password)

        verifyWithSuspend(exhaustive = false) { sessionRepository.saveSession(sessionToken, expiresIn, "ADMIN") }
    }

    @Test
    fun `GIVEN a successful login is made WHEN no user is found for userid THEN should not save the session`() = runTest {
        val username = "username"
        val password = "password"

        mockSuccessfulApiLogin()
        mockNotFoundGetUserApiCall()


        val result = loginRepository.login(username, password)

        verifyWithSuspend(exhaustive = true) {
            loginApi.login(username, password)
            loginApi.getUser("userId")
        }

        assertEquals(
            LoginException.NonExistentUserException,
            result.exceptionOrNull()
        )
    }

    @Test
    fun `GIVEN role is saved WHEN getRole called THEN returns correct UserRole`() = runTest {
        everySuspending { sessionRepository.getRole() } returns "PORTEIRO"

        val role = loginRepository.getRole()

        assertEquals(UserRole.PORTEIRO, role)
    }

    @Test
    fun `GIVEN no role saved WHEN getRole called THEN returns null`() = runTest {
        everySuspending { sessionRepository.getRole() } returns null

        val role = loginRepository.getRole()

        assertNull(role)
    }


    private suspend fun mockSuccessfulApiLogin() {
        val sessionToken = "sessionToken"
        val expiresIn = 10L
        val userId = "userId"
        val username = "username"
        val password = "password"

        everySuspending { loginApi.login(username, password) } returns LoginSessionDto(
            token = sessionToken,
            userId = userId,
            expiresIn = expiresIn
        )
    }

    private suspend fun justRunsSaveSession() {
        val sessionToken = "sessionToken"
        val expiresIn = 10L
        everySuspending { sessionRepository.saveSession(sessionToken, expiresIn, "ADMIN") } runs { }
    }

    private suspend fun mockSuccessGetUserApiCall() {
        val nome = "logged user name"
        val userId = "userId"
        val cpf = "cpf"
        val email = "email"

        everySuspending { loginApi.getUser(userId) } returns UserDto(nome, cpf, email, "ADMIN")
    }

    private suspend fun mockNotFoundGetUserApiCall() {
        val userId = "userId"

        everySuspending { loginApi.getUser(userId) } returns null
    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}
