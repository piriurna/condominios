package com.zalamena.condominios.condominio.domain.porteiro.usecase

import com.zalamena.condominios.condominio.domain.porteiro.models.PorteiroInfo
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroProvider

class GetPorteirosByCondominioUseCase(
    private val porteiroProvider: PorteiroProvider
) {

    suspend operator fun invoke(condominioId: String): Result<List<PorteiroInfo>> {
        if (condominioId.isBlank()) {
            return Result.failure(IllegalArgumentException("condominioId must not be blank"))
        }
        return porteiroProvider.getPorteirosByCondominioId(condominioId)
    }
}
