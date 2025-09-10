package com.zalamena.condominios.moradores.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.moradores.ui.models.MoradorUiData

@Composable
fun MoradorListItem(
    moradorUiData: MoradorUiData
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = moradorUiData.nome
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = moradorUiData.cpf
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = moradorUiData.apartamento.numero
        )
    }
}