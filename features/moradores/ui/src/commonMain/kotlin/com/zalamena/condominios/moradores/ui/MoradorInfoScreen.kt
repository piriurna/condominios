package com.zalamena.condominios.moradores.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MoradorInfoScreen(
    viewModel: MoradorInfoViewModel
) {
    MoradorInfoScreenContent(viewModel.uiState.value)
}


@Composable
private fun MoradorInfoScreenContent(
    uiState: UiState
) {
    if(uiState.morador != null) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.morador.nome
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = uiState.morador.apartamento.numero
            )
        }
    }
}


@Preview
@Composable
fun MoradorInfoScreenContentPreview() {
    MoradorInfoScreenContent(
        uiState = UiState(
            morador = MoradorUiData.dummy
        )
    )
}