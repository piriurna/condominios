package com.zalamena.condominios.condominio.domain.apartamento.models

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.pessoa.domain.models.Pessoa


interface ApartamentoProperties {
    val id: String
    val numero: String
    val andar: String

    val moradores: List<Morador>
}


data class Apartamento(
    override val id: String,
    override val numero: String,
    override val andar: String,
    override val moradores: List<Morador>
): ApartamentoProperties {

    companion object {
        val dummy: Apartamento by lazy {
            val apt = Apartamento(
                id = "id",
                numero = "numero",
                andar = "andar",
                moradores = emptyList()
            )
            apt.copy(
                moradores = listOf(
                    Morador(
                        pessoa = Pessoa.dummy,
                        apartamento = apt,
                        tipo = MoradorTipo.RESIDENTE
                    )
                )
            )
        }
    }
}
