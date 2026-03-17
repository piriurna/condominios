package com.zalamena.login.ui.createuser

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.CondominioValidator
import com.zalamena.login.domain.repository.UserRepository
import com.zalamena.login.domain.usecase.CreateUserUseCase
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CreateUserViewModelTest : TestsWithMocks() {

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var condominioValidator: CondominioValidator

    private val createUserUseCase by lazy { CreateUserUseCase(userRepository, condominioValidator) }
    private val viewModel by lazy { CreateUserViewModel(createUserUseCase) }

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
    fun `GIVEN initial state THEN fields are empty and no errors`() {
        val state = viewModel.uiState.value
        assertEquals("", state.name)
        assertEquals("", state.cpf)
        assertEquals("", state.email)
        assertEquals(RoleOption.PORTEIRO, state.selectedRole)
        assertEquals("", state.condominioId)
        assertTrue(!state.isLoading)
        assertNull(state.nameError)
        assertNull(state.cpfError)
        assertNull(state.emailError)
        assertNull(state.condominioIdError)
        assertNull(state.error)
        assertNull(state.generatedPassword)
        assertNull(state.navigationEvent)
    }

    @Test
    fun `GIVEN setName called THEN name is updated`() {
        viewModel.setName("Admin")
        assertEquals("Admin", viewModel.uiState.value.name)
    }

    @Test
    fun `GIVEN setCpf called THEN cpf is updated`() {
        viewModel.setCpf("12345678900")
        assertEquals("12345678900", viewModel.uiState.value.cpf)
    }

    @Test
    fun `GIVEN setEmail called THEN email is updated`() {
        viewModel.setEmail("admin@test.com")
        assertEquals("admin@test.com", viewModel.uiState.value.email)
    }

    @Test
    fun `GIVEN setRole called THEN role is updated`() {
        viewModel.setRole(RoleOption.ADMIN)
        assertEquals(RoleOption.ADMIN, viewModel.uiState.value.selectedRole)
    }

    @Test
    fun `GIVEN setCondominioId called THEN condominioId is updated`() {
        viewModel.setCondominioId("condo-1")
        assertEquals("condo-1", viewModel.uiState.value.condominioId)
    }

    @Test
    fun `GIVEN valid porteiro data WHEN createUser THEN generatedPassword is set`() = runTest {
        val role = UserRole.Porteiro("condo-1")
        val user = User(name = "Porteiro", cpf = "11111111111", email = "p@test.com", role = role)
        everySuspending { condominioValidator.exists("condo-1") } returns true
        everySuspending { userRepository.createUser("Porteiro", "11111111111", "p@test.com", role) } returns Result.success(user)

        viewModel.setName("Porteiro")
        viewModel.setCpf("11111111111")
        viewModel.setEmail("p@test.com")
        viewModel.setRole(RoleOption.PORTEIRO)
        viewModel.setCondominioId("condo-1")
        viewModel.createUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.generatedPassword)
        assertTrue(state.generatedPassword!!.length == 8)
        assertTrue(!state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `GIVEN valid admin data WHEN createUser THEN generatedPassword is set`() = runTest {
        val role = UserRole.Admin
        val user = User(name = "Admin", cpf = "12345678900", email = "a@test.com", role = role)
        everySuspending { userRepository.createUser("Admin", "12345678900", "a@test.com", role) } returns Result.success(user)

        viewModel.setName("Admin")
        viewModel.setCpf("12345678900")
        viewModel.setEmail("a@test.com")
        viewModel.setRole(RoleOption.ADMIN)
        viewModel.createUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.generatedPassword)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `GIVEN empty name WHEN createUser THEN nameError is set`() = runTest {
        viewModel.setCpf("12345678900")
        viewModel.setEmail("a@test.com")
        viewModel.setRole(RoleOption.ADMIN)
        viewModel.createUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.nameError)
        assertNull(state.generatedPassword)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `GIVEN empty cpf WHEN createUser THEN cpfError is set`() = runTest {
        viewModel.setName("Admin")
        viewModel.setEmail("a@test.com")
        viewModel.setRole(RoleOption.ADMIN)
        viewModel.createUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.cpfError)
        assertNull(state.generatedPassword)
    }

    @Test
    fun `GIVEN empty email WHEN createUser THEN emailError is set`() = runTest {
        viewModel.setName("Admin")
        viewModel.setCpf("12345678900")
        viewModel.setRole(RoleOption.ADMIN)
        viewModel.createUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
        assertNull(state.generatedPassword)
    }

    @Test
    fun `GIVEN porteiro without condominioId WHEN createUser THEN condominioIdError is set`() = runTest {
        viewModel.setName("Porteiro")
        viewModel.setCpf("11111111111")
        viewModel.setEmail("p@test.com")
        viewModel.setRole(RoleOption.PORTEIRO)
        viewModel.createUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.condominioIdError)
        assertNull(state.generatedPassword)
    }

    @Test
    fun `GIVEN reset called THEN state returns to initial`() {
        viewModel.setName("Test")
        viewModel.setCpf("123")
        viewModel.setEmail("test@test.com")

        viewModel.reset()

        val state = viewModel.uiState.value
        assertEquals("", state.name)
        assertEquals("", state.cpf)
        assertEquals("", state.email)
    }

    @Test
    fun `GIVEN onNavigateBack called THEN navigation event is set`() {
        viewModel.onNavigateBack()

        assertEquals(CreateUserNavigationEvent.NavigateBack, viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN onNavigationHandled called THEN navigation event is cleared`() {
        viewModel.onNavigateBack()
        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.navigationEvent)
    }

    @Test
    fun `GIVEN nameError exists WHEN setName THEN error is cleared`() = runTest {
        viewModel.setCpf("12345678900")
        viewModel.setEmail("a@test.com")
        viewModel.setRole(RoleOption.ADMIN)
        viewModel.createUser()
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.nameError)

        viewModel.setName("Admin")
        assertNull(viewModel.uiState.value.nameError)
    }
}
