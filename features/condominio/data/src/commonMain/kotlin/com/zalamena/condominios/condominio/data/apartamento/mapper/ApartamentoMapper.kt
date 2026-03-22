package com.zalamena.condominios.condominio.data.apartamento.mapper

import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoWithAllData
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.pessoa.data.mapper.toDomain

fun ApartamentoWithAllData.toDomain(): Apartamento {
    val apt = Apartamento(
        id = apartamento.id,
        numero = apartamento.numero,
        andar = apartamento.andar,
        moradores = emptyList()
    )
    return apt.copy(
        moradores = moradores.map { moradorWithPessoa ->
            Morador(
                pessoa = moradorWithPessoa.pessoa.toDomain(),
                apartamento = apt,
                tipo = runCatching { MoradorTipo.valueOf(moradorWithPessoa.morador.tipo) }
                    .getOrDefault(MoradorTipo.RESIDENTE)
            )
        }
    )
}

fun ApartamentoEntity.toDomain(): Apartamento {
    return Apartamento(
        id = id,
        numero = numero,
        andar = andar,
        moradores = emptyList()
    )
}

fun Apartamento.toEntity(condominioId: String): ApartamentoEntity {
    return ApartamentoEntity(
        id = id,
        numero = numero,
        andar = andar,
        condominioId = condominioId
    )
}
