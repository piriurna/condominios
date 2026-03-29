package com.zalamena.condominios.condominio.ui.condominio.dashboard.models

data class CondominioSummaryUiData(
    val id: String,
    val nome: String,
    val enderecoDescription: String,
    val apartamentoCount: Int = 0
)
