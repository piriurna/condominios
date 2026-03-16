package com.zalamena.login.data.repository

import com.zalamena.login.domain.models.UserRole
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRepositoryTest {

    private val repository = UserRepositoryImpl()

    @Test
    fun `GIVEN valid admin data WHEN createUser THEN returns success with user`() = runTest {
        val result = repository.createUser("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null)

        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertEquals("Admin", user.name)
        assertEquals("12345678900", user.cpf)
        assertEquals("admin@test.com", user.email)
        assertEquals(UserRole.ADMIN, user.role)
        assertEquals(null, user.condominioId)
    }

    @Test
    fun `GIVEN valid porteiro data WHEN createUser THEN returns success with condominioId`() = runTest {
        val result = repository.createUser("Porteiro", "11111111111", "porteiro@test.com", UserRole.PORTEIRO, "condo-1")

        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertEquals("Porteiro", user.name)
        assertEquals(UserRole.PORTEIRO, user.role)
        assertEquals("condo-1", user.condominioId)
    }

    @Test
    fun `GIVEN users created WHEN getUsers THEN returns all created users`() = runTest {
        repository.createUser("Admin", "12345678900", "admin@test.com", UserRole.ADMIN, null)
        repository.createUser("Porteiro", "11111111111", "porteiro@test.com", UserRole.PORTEIRO, "condo-1")

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
