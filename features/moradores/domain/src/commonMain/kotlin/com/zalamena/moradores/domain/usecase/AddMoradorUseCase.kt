package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository

class AddMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {


    operator fun invoke(morador: Morador): Result<Unit> {
        val existingMorador = moradoresRepository.getMorador(morador)

        if(existingMorador == null) {
            moradoresRepository.addMorador(morador)
            return Result.success(Unit)
        }

        return Result.failure(Exception("Morador já existe"))

    }
}