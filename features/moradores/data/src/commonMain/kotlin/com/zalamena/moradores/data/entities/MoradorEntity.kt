package com.zalamena.moradores.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.zalamena.condominios.apartamentos.data.entity.ApartamentoEntity
import com.zalamena.condominios.individuo.data.entities.IndividuoEntity


@Entity(
    tableName = "Morador",
    foreignKeys = [
        ForeignKey(
            entity = IndividuoEntity::class,
            parentColumns = ["id"],
            childColumns = ["individuoId"],
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
    val individuoId: String,
    val apartamentoId: String,
) {


    companion object {
        val dummy = MoradorEntity(
            id = 1L,
            individuoId = "id",
            apartamentoId = "id"
        )
    }
}

data class MoradorWithIndividuoAndApartamentoEntity(
    @Embedded val morador: MoradorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "individuoId"
    )
    val individuo: IndividuoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "apartamentoId"
    )
    val apartamento: ApartamentoEntity
) {

    companion object {
        val dummy = MoradorWithIndividuoAndApartamentoEntity(
            morador = MoradorEntity.dummy,
            individuo = IndividuoEntity.dummy,
            apartamento = ApartamentoEntity.dummy
        )
    }
}