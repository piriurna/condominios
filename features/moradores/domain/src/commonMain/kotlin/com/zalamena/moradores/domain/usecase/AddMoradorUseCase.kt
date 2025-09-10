package com.zalamena.moradores.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.models.MoradorException
import com.zalamena.moradores.domain.repository.MoradoresRepository

class AddMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(morador: Individuo, apartamento: Apartamento): Result<Unit> {
        val existingMoradorResult = moradoresRepository.getMorador(morador.cpf, apartamento.id)

        if(existingMoradorResult.exceptionOrNull() == MoradorException.MoradorNotFoundException) {
            moradoresRepository.addMorador(morador, apartamento)
            return Result.success(Unit)
        }

        return Result.failure(existingMoradorResult.exceptionOrNull()?: Exception("Erro ao adicionar morador"))
    }
}