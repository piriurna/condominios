package com.zalamena.moradores.domain

import com.zalamena.condominios.moradores.domain.models.Morador

sealed class LoginResult {

    data class Success(val morador: Morador): LoginResult()
    data class Error(val message: String): LoginResult()
}