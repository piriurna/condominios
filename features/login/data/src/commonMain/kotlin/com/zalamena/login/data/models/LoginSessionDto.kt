package com.zalamena.login.data.models

data class LoginSessionDto(
    val token: String,
    val userId: String,
    val expiresIn: Long
)