package com.zalamena.condominios.moradores.domain

import com.zalamena.condominios.common.AppResult
import com.zalamena.condominios.moradores.domain.models.Morador

sealed class LoginResult: AppResult {

    data class Success(val morador: Morador): LoginResult()
    data class Error(val message: String): LoginResult()
}