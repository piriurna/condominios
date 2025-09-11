package com.zalamena.moradores.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.zalamena.condominios.apartamentos.data.entity.ApartamentoEntity
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity


@Entity(
    tableName = "Morador",
    foreignKeys = [
        ForeignKey(
            entity = PessoaEntity::class,
            parentColumns = ["id"],
            childColumns = ["pessoaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ApartamentoEntity::class,
            parentColumns = ["id"],
            childColumns = ["apartamentoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MoradorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val pessoaId: String,
    val apartamentoId: String,
) {


    companion object {
        val dummy = MoradorEntity(
            id = 1L,
            pessoaId = "id",
            apartamentoId = "id"
        )
    }
}

data class MoradorWithPessoaAndApartamentoEntity(
    @Embedded val morador: MoradorEntity,
    @Relation(
        parentColumn = "pessoaId",
        entityColumn = "id"
    )
    val pessoa: PessoaEntity,
    @Relation(
        parentColumn = "apartamentoId",
        entityColumn = "id"
    )
    val apartamento: ApartamentoEntity
) {

    companion object {
        val dummy = MoradorWithPessoaAndApartamentoEntity(
            morador = MoradorEntity.dummy,
            pessoa = PessoaEntity.dummy,
            apartamento = ApartamentoEntity.dummy
        )
    }
}