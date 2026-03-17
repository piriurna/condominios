package com.zalamena.condominios.condominio.domain.porteiro.repository

import com.zalamena.condominios.condominio.domain.porteiro.models.PorteiroInfo

fun interface PorteiroProvider {
    suspend fun getPorteirosByCondominioId(condominioId: String): Result<List<PorteiroInfo>>
}
