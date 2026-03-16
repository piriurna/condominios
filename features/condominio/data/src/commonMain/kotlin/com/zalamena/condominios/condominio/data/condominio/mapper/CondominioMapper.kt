package com.zalamena.condominios.condominio.data.condominio.mapper

import com.zalamena.condominios.condominio.data.apartamento.mapper.toDomain
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioEntity
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioWithAllData
import com.zalamena.condominios.condominio.data.condominio.entity.EnderecoEntity
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.models.Endereco

fun CondominioWithAllData.toCondominio(): Condominio {
    return Condominio(
        id = condominio.id,
        nome = condominio.nome,
        endereco = endereco.toDomain(),
        apartamentos = apartamentos.map { it.toDomain() }
    )
}

fun EnderecoEntity.toDomain(): Endereco {
    return Endereco(
        rua = rua,
        numero = numero,
        cep = cep,
        cidade = cidade,
        estado = estado

    )
}

fun Endereco.toEntity(): EnderecoEntity {
    return EnderecoEntity(
        id = "$cep-$numero-$estado-$cidade",
        rua = rua,
        numero = numero,
        cep = cep,
        cidade = cidade,
        estado = estado
    )
}
fun Condominio.toEntity(
    enderecoId: String
): CondominioEntity {
    return CondominioEntity(
        id = id,
        nome = nome,
        enderecoId = enderecoId,
    )
}