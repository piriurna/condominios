package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository

class LoginMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
){
    operator fun invoke(morador: Morador): Result<Morador> {
        val existingMorador = moradoresRepository.getMorador(morador.cpf)
        val loggedInMorador = moradoresRepository.getCurrentLoggedMorador()

        if(loggedInMorador != null) {
            return Result.failure(Exception("Já existe um usuário logado"))
        }

        if(existingMorador == null) {
            moradoresRepository.addMorador(morador)
        }

        moradoresRepository.loginMorador(morador)

        return Result.success(morador)
    }
}