package com.zalamena.condominios.condominio.ui.apartamento.detail

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.MoradorDetailUiData

@Composable
fun ApartamentoDetailScreen(
    viewModel: ApartamentoDetailViewModel,
    isAdminMode: Boolean = true,
    onNavigateToAddMorador: (apartamentoId: String) -> Unit = {},
    onNavigateToMoradorDetail: (pessoaId: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.load()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is ApartamentoDetailNavEvent.AddMorador -> {
                onNavigateToAddMorador(event.apartamentoId)
                viewModel.onNavigationHandled()
            }
            is ApartamentoDetailNavEvent.MoradorDetail -> {
                onNavigateToMoradorDetail(event.pessoaId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.isError -> {
                Text(
                    text = "Erro ao carregar apartamento",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                ApartamentoDetailContent(
                    uiState = uiState,
                    isAdminMode = isAdminMode,
                    onAddMoradorClick = viewModel::onAddMoradorClick,
                    onMoradorClick = viewModel::onMoradorClick
                )
            }
        }
    }
}

@Composable
private fun ApartamentoDetailContent(
    uiState: ApartamentoDetailUiState,
    isAdminMode: Boolean = true,
    onAddMoradorClick: () -> Unit = {},
    onMoradorClick: (pessoaId: String) -> Unit = {}
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
                items(uiState.moradores) { morador ->
                    MoradorRow(morador = morador, onClick = { onMoradorClick(morador.pessoaId) })
                }
            }
        }

        if (isAdminMode) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onAddMoradorClick, modifier = Modifier.fillMaxWidth()) {
                Text("+ Adicionar Morador")
            }
        }
    }
}

@Composable
private fun MoradorRow(morador: MoradorDetailUiData, onClick: () -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(morador.nome, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = morador.tipoLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = morador.maskedCpf,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
