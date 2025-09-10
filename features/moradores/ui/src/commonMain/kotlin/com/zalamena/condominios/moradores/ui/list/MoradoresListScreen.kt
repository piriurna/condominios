package com.zalamena.condominios.moradores.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.moradores.ui.components.MoradorListItem
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MoradoresListScreen(
    viewModel: MoradoresListViewModel
) {
    val uiState = viewModel.uiState.value

    MoradoresListScreenContent(uiState)
}



@Composable
fun MoradoresListScreenContent(
    uiState: MoradoresListUiState
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.moradores) {
            MoradorListItem(it)
        }

    }
}

@Preview
@Composable
fun MoradoresListScreenContentPreview() {
    MoradoresListScreenContent(
        uiState = MoradoresListUiState(
            moradores = listOf(
                MoradorUiData.dummy,
                MoradorUiData.dummy,
                MoradorUiData.dummy,
                MoradorUiData.dummy,
                MoradorUiData.dummy,
            )
        )
    )
}