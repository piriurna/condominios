package com.zalamena.condominios.condominio.ui.apartamento.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading

@Composable
fun ApartamentoDetailScreen(
    viewModel: ApartamentoDetailViewModel,
    onNavigateToAddMorador: (apartamentoId: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.apartamentoId) {
        viewModel.load()
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is ApartamentoDetailNavEvent.AddMorador -> {
                onNavigateToAddMorador(event.apartamentoId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    ApartamentoDetailContent(
        uiState = uiState,
        onAddMoradorClick = viewModel::onAddMoradorClick
    )

    FullscreenLoading(isLoading = uiState.isLoading)
}

@Composable
private fun ApartamentoDetailContent(
    uiState: ApartamentoDetailUiState,
    onAddMoradorClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Apartamento ${uiState.numero}", style = MaterialTheme.typography.headlineMedium)
        Text("Andar ${uiState.andar}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Text("Moradores", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (uiState.moradores.isEmpty()) {
            Text("Nenhum morador cadastrado.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.moradores) { nome ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = nome,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddMoradorClick, modifier = Modifier.fillMaxWidth()) {
            Text("+ Adicionar Morador")
        }
    }
}
