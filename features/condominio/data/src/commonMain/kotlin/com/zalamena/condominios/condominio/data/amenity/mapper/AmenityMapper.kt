package com.zalamena.condominios.condominio.data.amenity.mapper

import com.zalamena.condominios.condominio.data.amenity.entity.AmenityEntity
import com.zalamena.condominios.condominio.domain.amenity.model.Amenity

fun AmenityEntity.toDomain(): Amenity = Amenity(
    id = id,
    condominioId = condominioId,
    nome = nome,
    descricao = descricao,
    capacidade = capacidade,
    precoUso = precoUso,
    requerAprovacaoSindico = requerAprovacaoSindico
)

fun Amenity.toEntity(): AmenityEntity = AmenityEntity(
    id = id,
    condominioId = condominioId,
    nome = nome,
    descricao = descricao,
    capacidade = capacidade,
    precoUso = precoUso,
    requerAprovacaoSindico = requerAprovacaoSindico
)
