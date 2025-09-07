package com.zalamena.condominios.moradores.domain.usecase

import com.zalamena.condominios.moradores.domain.LoginResult
import com.zalamena.condominios.moradores.domain.models.Morador
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository

class LoginMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
){
    operator fun invoke(morador: Morador): Result<Morador> {
        val existingMorador = moradoresRepository.getMorador(morador)

        if(existingMorador == null) {
            moradoresRepository.addMorador(morador)
        }

        moradoresRepository.loginMorador(morador)

        return Result.success(morador)
    }
}