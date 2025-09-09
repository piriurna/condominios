package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.models.MoradorException
import com.zalamena.moradores.domain.repository.MoradoresRepository

class AddMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    operator fun invoke(morador: Morador): Result<Unit> {
        val existingMoradorResult = moradoresRepository.getMorador(morador.cpf)

        if(existingMoradorResult.isFailure && existingMoradorResult.exceptionOrNull() == MoradorException.MoradorNotFoundException) {
            moradoresRepository.addMorador(morador)
            return Result.success(Unit)
        }

        return Result.failure(Exception("Morador já existe"))

    }
}