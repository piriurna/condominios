package com.zalamena.login.data.api

import com.zalamena.login.data.models.LoginSessionDto
import com.zalamena.login.data.models.UserDto

interface LoginApi {

    suspend fun login(username: String, password: String): LoginSessionDto


    suspend fun getUser(userId: String): UserDto?
}