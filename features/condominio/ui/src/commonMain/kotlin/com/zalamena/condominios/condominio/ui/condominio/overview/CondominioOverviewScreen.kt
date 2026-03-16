package com.zalamena.condominios.condominio.ui.condominio.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading

@Composable
fun CondominioOverviewScreen(
    viewModel: CondominioOverviewViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value

    FullscreenLoading(isLoading = uiState.isLoading)
    when {
        !uiState.isLoading && !uiState.isError -> {
            CondominioOverviewScreenContent(uiState)
        }

        else -> {}
    }
}

@Composable
fun CondominioOverviewScreenContent(
    uiState: CondominioOverviewUiState
) {
    if (uiState.condominio != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(uiState.condominio.apartamentos) {
                    // Apartamento section with a 30% opacity of a color, the number of the apartment and the list of moradores inside
                }
            }
        }
    }
}