package com.zalamena.condominios.condominio.domain.amenity.model

data class Amenity(
    val id: String,
    val condominioId: String,
    val nome: String,
    val descricao: String = "",
    val capacidade: Int? = null,
    val precoUso: Double? = null,
    val requerAprovacaoSindico: Boolean = false
) {
    companion object {
        val dummy = Amenity(
            id = "amenity-1",
            condominioId = "condo-1",
            nome = "Salao de Festas",
            descricao = "Salao para eventos",
            capacidade = 50,
            precoUso = 200.0,
            requerAprovacaoSindico = false
        )
    }
}
