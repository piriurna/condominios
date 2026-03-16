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
    fun `GIVEN valid admin data WHEN createUser THEN returns success with user`() = runTest {
        val role = UserRole.Admin
        val result = repository.createUser("Admin", "12345678900", "admin@test.com", role)

        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertEquals("Admin", user.name)
        assertEquals("12345678900", user.cpf)
        assertEquals("admin@test.com", user.email)
        assertIs<UserRole.Admin>(user.role)
    }

    @Test
    fun `GIVEN valid porteiro data WHEN createUser THEN returns success with condominioId`() = runTest {
        val role = UserRole.Porteiro("condo-1")
        val result = repository.createUser("Porteiro", "11111111111", "porteiro@test.com", role)

        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertEquals("Porteiro", user.name)
        val userRole = user.role
        assertIs<UserRole.Porteiro>(userRole)
        assertEquals("condo-1", userRole.condominioId)
    }

    @Test
    fun `GIVEN users created WHEN getUsers THEN returns all created users`() = runTest {
        repository.createUser("Admin", "12345678900", "admin@test.com", UserRole.Admin)
        repository.createUser("Porteiro", "11111111111", "porteiro@test.com", UserRole.Porteiro("condo-1"))

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
}
