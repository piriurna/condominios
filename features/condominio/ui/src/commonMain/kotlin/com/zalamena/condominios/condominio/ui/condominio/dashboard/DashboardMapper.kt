package com.zalamena.condominios.condominio.ui.condominio.dashboard

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.ApartamentoDashboardUiData
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.CondominioSummaryUiData

fun Condominio.toSummaryUi(): CondominioSummaryUiData = CondominioSummaryUiData(
    id = id,
    nome = nome,
    enderecoDescription = "${endereco.rua}, ${endereco.numero} — ${endereco.cidade}/${endereco.estado}"
)

fun Apartamento.toDashboardUi(): ApartamentoDashboardUiData = ApartamentoDashboardUiData(
    id = id,
    numero = numero,
    andar = andar,
    moradorCount = moradores.size,
    isOcupado = moradores.isNotEmpty()
)
