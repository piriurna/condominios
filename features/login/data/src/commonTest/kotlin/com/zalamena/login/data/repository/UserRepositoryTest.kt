package com.zalamena.login.data.repository

import com.zalamena.login.domain.models.UserRole
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UserRepositoryTest {

    private val repository = UserRepositoryImpl()

    @Test
    fun `GIVEN valid admin data WHEN createUser THEN returns success with user and generated id`() = runTest {
        val role = UserRole.Admin
        val result = repository.createUser("Admin", "12345678900", "admin@test.com", role, "pass123")

        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertTrue(user.id.isNotBlank())
        assertEquals("Admin", user.name)
        assertEquals("12345678900", user.cpf)
        assertEquals("admin@test.com", user.email)
        assertIs<UserRole.Admin>(user.role)
    }

    @Test
    fun `GIVEN valid porteiro data WHEN createUser THEN returns success with condominioId`() = runTest {
        val role = UserRole.Porteiro("condo-1")
        val result = repository.createUser("Porteiro", "11111111111", "porteiro@test.com", role, "pass123")

        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertTrue(user.id.isNotBlank())
        assertEquals("Porteiro", user.name)
        val userRole = user.role
        assertIs<UserRole.Porteiro>(userRole)
        assertEquals("condo-1", userRole.condominioId)
    }

    @Test
    fun `GIVEN users created WHEN getUsers THEN returns all created users`() = runTest {
        repository.createUser("Admin", "12345678900", "admin@test.com", UserRole.Admin, "pass123")
        repository.createUser("Porteiro", "11111111111", "porteiro@test.com", UserRole.Porteiro("condo-1"), "pass123")

        val result = repository.getUsers()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `GIVEN no users created WHEN getUsers THEN returns empty list`() = runTest {
        val result = repository.getUsers()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN two users created THEN each has a unique id`() = runTest {
        val user1 = repository.createUser("A", "111", "a@test.com", UserRole.Admin, "pass123").getOrThrow()
        val user2 = repository.createUser("B", "222", "b@test.com", UserRole.Admin, "pass123").getOrThrow()

        assertTrue(user1.id != user2.id)
    }

    // --- getUsersByCondominioId ---

    @Test
    fun `GIVEN porteiros in different condominios WHEN getUsersByCondominioId THEN returns only matching`() = runTest {
        repository.createUser("P1", "111", "p1@test.com", UserRole.Porteiro("condo-1"), "pass123")
        repository.createUser("P2", "222", "p2@test.com", UserRole.Porteiro("condo-2"), "pass123")
        repository.createUser("P3", "333", "p3@test.com", UserRole.Porteiro("condo-1"), "pass123")

        val result = repository.getUsersByCondominioId("condo-1")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
        assertTrue(result.getOrThrow().all { (it.role as UserRole.Porteiro).condominioId == "condo-1" })
    }

    @Test
    fun `GIVEN admin users WHEN getUsersByCondominioId THEN returns empty list`() = runTest {
        repository.createUser("Admin", "111", "a@test.com", UserRole.Admin, "pass123")

        val result = repository.getUsersByCondominioId("condo-1")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN no users WHEN getUsersByCondominioId THEN returns empty list`() = runTest {
        val result = repository.getUsersByCondominioId("condo-1")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    // --- deleteUser ---

    @Test
    fun `GIVEN user exists WHEN deleteUser THEN user is removed`() = runTest {
        val user = repository.createUser("Admin", "111", "a@test.com", UserRole.Admin, "pass123").getOrThrow()

        val result = repository.deleteUser(user.id)

        assertTrue(result.isSuccess)
        assertEquals(0, repository.getUsers().getOrThrow().size)
    }

    @Test
    fun `GIVEN user does not exist WHEN deleteUser THEN returns failure`() = runTest {
        val result = repository.deleteUser("nonexistent-id")

        assertTrue(result.isFailure)
        assertIs<NoSuchElementException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN multiple users WHEN deleteUser THEN only target is removed`() = runTest {
        val user1 = repository.createUser("A", "111", "a@test.com", UserRole.Admin, "pass123").getOrThrow()
        repository.createUser("B", "222", "b@test.com", UserRole.Admin, "pass123")

        repository.deleteUser(user1.id)

        val remaining = repository.getUsers().getOrThrow()
        assertEquals(1, remaining.size)
        assertEquals("B", remaining.first().name)
    }

    // --- updateUserRole ---

    @Test
    fun `GIVEN user exists WHEN updateUserRole THEN role is updated`() = runTest {
        val user = repository.createUser("P", "111", "p@test.com", UserRole.Porteiro("condo-1"), "pass123").getOrThrow()

        val result = repository.updateUserRole(user.id, UserRole.Porteiro("condo-2"))

        assertTrue(result.isSuccess)
        val updated = repository.getUsers().getOrThrow().first { it.id == user.id }
        val role = updated.role
        assertIs<UserRole.Porteiro>(role)
        assertEquals("condo-2", role.condominioId)
    }

    @Test
    fun `GIVEN user does not exist WHEN updateUserRole THEN returns failure`() = runTest {
        val result = repository.updateUserRole("nonexistent-id", UserRole.Admin)

        assertTrue(result.isFailure)
        assertIs<NoSuchElementException>(result.exceptionOrNull())
    }

    @Test
    fun `GIVEN user exists WHEN updateUserRole THEN other fields are preserved`() = runTest {
        val user = repository.createUser("P", "111", "p@test.com", UserRole.Porteiro("condo-1"), "pass123").getOrThrow()

        repository.updateUserRole(user.id, UserRole.Porteiro("condo-2"))

        val updated = repository.getUsers().getOrThrow().first { it.id == user.id }
        assertEquals("P", updated.name)
        assertEquals("111", updated.cpf)
        assertEquals("p@test.com", updated.email)
    }
}
