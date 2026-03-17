package com.zalamena.condominios.condominio.ui.condominio.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.ApartamentoDashboardUiData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CondominioDashboardScreen(
    viewModel: CondominioDashboardViewModel,
    onNavigateToAddApartamento: (condominioId: String) -> Unit = {},
    onNavigateToApartamento: (apartamentoId: String) -> Unit = {},
    onNavigateToCreatePorteiro: (condominioId: String) -> Unit = {},
    onNavigateToPorteiroList: (condominioId: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadCondominios()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
            is DashboardNavigationEvent.CreatePorteiro -> {
                onNavigateToCreatePorteiro(event.condominioId)
                viewModel.onNavigationHandled()
            }
            is DashboardNavigationEvent.PorteiroList -> {
                onNavigateToPorteiroList(event.condominioId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(uiState.condominioNome.ifBlank { "Apartamentos" })
                        if (uiState.condominioId.isNotBlank()) {
                            Text(
                                text = uiState.condominioId,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    Button(onClick = { viewModel.onPorteiroListClick() }) {
                        Text("Porteiros")
                    }
                    Button(onClick = { viewModel.onCreatePorteiroClick() }) {
                        Text("Criar Porteiro")
                    }
                }
            )
        },
        floatingActionButton = {
            if (!uiState.isLoading && !uiState.isError) {
                FloatingActionButton(onClick = { viewModel.onAddApartamentoClick() }) {
                    Text("+")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.isError -> {
                    Text(
                        text = "Erro ao carregar apartamentos",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.apartamentos.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Nenhum apartamento cadastrado")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.onAddApartamentoClick() }) {
                            Text("Criar primeiro apartamento")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item { Spacer(Modifier.height(8.dp)) }
                        item {
                            Text("Total: ${uiState.totalApartamentos} apartamento(s)")
                            Spacer(Modifier.height(8.dp))
                        }
                        items(uiState.apartamentos) { apt ->
                            ApartamentoCard(apt, onClick = { viewModel.onApartamentoClick(apt.id) })
                        }
                    }
                }
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
