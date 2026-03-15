package com.zalamena.condominios.condominio.data.apartamento.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntityWithPessoa


@Entity(tableName = "Apartamento")
data class ApartamentoEntity(
    @PrimaryKey
    val id: String,
    val numero: String,
    val andar: String,
    val condominioId: String
) {
    companion object {
        val dummy = ApartamentoEntity(
            id = "1",
            numero = "1",
            andar = "1",
            condominioId = "1"
        )

    }
}


data class ApartamentoWithAllData(
    @Embedded
    val apartamento: ApartamentoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "apartamentoId",
        entity = MoradorEntity::class
    )
    val moradores: List<MoradorEntityWithPessoa>
) {

    companion object {
        val dummy = ApartamentoWithAllData(
            apartamento = ApartamentoEntity.dummy,
            moradores = listOf(MoradorEntityWithPessoa.dummy)
        )

    }
}