package com.zalamena.condominios.condominio.ui.condominio.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.ApartamentoDashboardUiData
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.CondominioSummaryUiData

@Composable
fun CondominioDashboardScreen(
    viewModel: CondominioDashboardViewModel,
    onNavigateToAddApartamento: (condominioId: String) -> Unit = {},
    onNavigateToApartamento: (apartamentoId: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCondominios()
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is DashboardNavigationEvent.AddApartamento -> {
                onNavigateToAddApartamento(event.condominioId)
                viewModel.onNavigationHandled()
            }
            is DashboardNavigationEvent.ApartamentoDetails -> {
                onNavigateToApartamento(event.apartamentoId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    CondominioDashboardContent(
        uiState = uiState,
        onCondominioSelected = viewModel::selectCondominio,
        onAddApartamentoClick = viewModel::onAddApartamentoClick,
        onApartamentoClick = viewModel::onApartamentoClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CondominioDashboardContent(
    uiState: CondominioDashboardUiState,
    onCondominioSelected: (String) -> Unit = {},
    onAddApartamentoClick: () -> Unit = {},
    onApartamentoClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        CondominioSelector(
            condominios = uiState.condominios,
            selectedId = uiState.selectedCondominioId,
            onSelected = onCondominioSelected
        )

        if (uiState.selectedCondominioId != null) {
            Spacer(Modifier.height(12.dp))
            Text("Total: ${uiState.totalApartamentos} apartamento(s)")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onAddApartamentoClick, modifier = Modifier.fillMaxWidth()) {
                Text("+ Adicionar Apartamento")
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.apartamentos) { apt ->
                    ApartamentoCard(apt, onClick = { onApartamentoClick(apt.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CondominioSelector(
    condominios: List<CondominioSummaryUiData>,
    selectedId: String?,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = condominios.find { it.id == selectedId }?.nome ?: "Selecione um condomínio"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Condomínio") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            condominios.forEach { condominio ->
                DropdownMenuItem(
                    text = { Text(condominio.nome) },
                    onClick = {
                        onSelected(condominio.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ApartamentoCard(apt: ApartamentoDashboardUiData, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Apt ${apt.numero}", style = MaterialTheme.typography.titleMedium)
                Text("Andar ${apt.andar}", style = MaterialTheme.typography.bodySmall)
            }
            Text("${apt.moradorCount} morador(es)", style = MaterialTheme.typography.bodySmall)
        }
    }
}
