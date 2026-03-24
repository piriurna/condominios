package com.zalamena.condominios.condominio.ui.moradores.details

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.toLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoradorInfoScreen(
    viewModel: MoradorInfoViewModel,
    isAdminMode: Boolean = false,
    onNavigateToApartamento: (apartamentoId: String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is MoradorInfoNavigationEvent.ApartamentoDetails -> {
                onNavigateToApartamento(event.apartamentoId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Morador") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.isError -> {
                    Text(
                        text = "Erro ao carregar morador",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    MoradorDetailContent(
                        uiState = uiState,
                        isAdminMode = isAdminMode,
                        onApartamentoClick = viewModel::onApartamentoClick,
                        onSaveTipo = viewModel::onSaveTipo
                    )
                }
            }
        }
    }
}

@Composable
private fun MoradorDetailContent(
    uiState: MoradorDetailUiState,
    isAdminMode: Boolean,
    onApartamentoClick: (apartamentoId: String) -> Unit,
    onSaveTipo: (apartamentoId: String, newTipo: MoradorTipo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(uiState.nome, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(uiState.maskedCpf, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("Email: ${uiState.email}", style = MaterialTheme.typography.bodyMedium)
        Text("Telefone: ${uiState.telefone}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        Text("Apartamentos", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (uiState.tipoError != null) {
            Text(
                text = uiState.tipoError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(8.dp))
        }

        if (uiState.apartamentos.isEmpty()) {
            Text("Nenhum apartamento vinculado.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.apartamentos) { apt ->
                    ApartamentoDoMoradorCard(
                        apt = apt,
                        isAdminMode = isAdminMode,
                        onClick = { onApartamentoClick(apt.apartamentoId) },
                        onSaveTipo = { newTipo -> onSaveTipo(apt.apartamentoId, newTipo) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApartamentoDoMoradorCard(
    apt: ApartamentoDoMoradorUiData,
    isAdminMode: Boolean,
    onClick: () -> Unit,
    onSaveTipo: (MoradorTipo) -> Unit
) {
    var selectedTipo by remember(apt.apartamentoId, apt.tipo) { mutableStateOf(apt.tipo) }
    var expanded by remember { mutableStateOf(false) }
    val hasChanged = selectedTipo != apt.tipo

    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Apt ${apt.numero}", style = MaterialTheme.typography.titleSmall)
                    Text("Andar ${apt.andar}", style = MaterialTheme.typography.bodySmall)
                }
                if (!isAdminMode) {
                    Text(
                        text = apt.tipoLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (isAdminMode) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedTipo.toLabel(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            MoradorTipo.entries.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.toLabel()) },
                                    onClick = {
                                        selectedTipo = tipo
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    if (hasChanged) {
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { onSaveTipo(selectedTipo) }) {
                            Text("Salvar")
                        }
                    }
                }
            }
        }
    }
}
