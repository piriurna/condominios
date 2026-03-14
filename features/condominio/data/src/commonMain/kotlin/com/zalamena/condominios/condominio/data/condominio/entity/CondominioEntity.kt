package com.zalamena.condominios.condominio.data.condominio.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoWithAllData

@Entity(
    tableName = "Condominio",
    foreignKeys = [
        ForeignKey(
            entity = EnderecoEntity::class,
            parentColumns = ["id"],
            childColumns = ["enderecoId"],
        )
    ]
)
data class CondominioEntity(
    val id: String,
    val nome: String,
    val enderecoId: String,
) {

    companion object {
        val dummy = CondominioEntity(
            id = "id",
            nome = "nome",
            enderecoId = "enderecoId"
        )
    }
}


data class CondominioWithAllData(
    @Embedded
    val condominio: CondominioEntity,
    @Relation(
        parentColumn = "enderecoId",
        entityColumn = "id"
    )
    val endereco: EnderecoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "condominioId"
    )
    val apartamentos: List<ApartamentoWithAllData>
) {

    companion object {
        val dummy = CondominioWithAllData(
            condominio = CondominioEntity.dummy,
            endereco = EnderecoEntity.dummy,
            apartamentos = listOf(ApartamentoWithAllData.dummy)
        )
    }
}