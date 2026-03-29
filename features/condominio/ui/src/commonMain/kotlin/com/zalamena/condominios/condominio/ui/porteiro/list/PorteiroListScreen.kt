package com.zalamena.condominios.condominio.ui.porteiro.list

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.zalamena.condominios.common.ui.components.EmptyState
import com.zalamena.condominios.common.ui.components.scaffold.LoadingScaffold
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.maskCpf
import com.zalamena.condominios.condominio.ui.condominio.dashboard.models.CondominioSummaryUiData
import com.zalamena.condominios.condominio.ui.porteiro.list.models.PorteiroUiData

@Composable
fun PorteiroListScreen(
    viewModel: PorteiroListViewModel,
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadPorteiros()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LoadingScaffold(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        title = "Porteiros",
        errorMessage = "Erro ao carregar porteiros",
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }
    ) {
        if (uiState.porteiros.isEmpty()) {
            EmptyState(
                message = "Nenhum porteiro cadastrado",
                icon = Icons.Default.Badge
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(uiState.porteiros) { porteiro ->
                    PorteiroCard(
                        porteiro = porteiro,
                        onReassignClick = { viewModel.onReassignClick(porteiro) },
                        onDeleteClick = { viewModel.onDeleteClick(porteiro) }
                    )
                }
            }
        }
    }

    uiState.showDeleteConfirmation?.let { porteiro ->
        DeleteConfirmationDialog(
            porteiroName = porteiro.name,
            onConfirm = { viewModel.onConfirmDelete() },
            onDismiss = { viewModel.onDismissDelete() }
        )
    }

    uiState.showReassignDialog?.let { porteiro ->
        ReassignDialog(
            porteiroName = porteiro.name,
            condominios = uiState.availableCondominios,
            onConfirm = { newCondominioId -> viewModel.onConfirmReassign(newCondominioId) },
            onDismiss = { viewModel.onDismissReassign() }
        )
    }
}

@Composable
private fun PorteiroCard(
    porteiro: PorteiroUiData,
    onReassignClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Badge,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Text(porteiro.name, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(4.dp))
            Text("CPF: ${maskCpf(porteiro.cpf)}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(4.dp))
                Text(porteiro.email, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onReassignClick) {
                    Text("Reatribuir")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    porteiroName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir porteiro") },
        text = { Text("Tem certeza que deseja excluir o porteiro $porteiroName?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Excluir", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReassignDialog(
    porteiroName: String,
    condominios: List<CondominioSummaryUiData>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCondominioId by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reatribuir porteiro") },
        text = {
            Column {
                Text("Selecione o novo condominio para $porteiroName:")
                Spacer(Modifier.height(12.dp))
                if (condominios.isEmpty()) {
                    Text(
                        "Nenhum outro condominio disponivel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Condominio") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            condominios.forEach { condominio ->
                                DropdownMenuItem(
                                    text = { Text(condominio.nome) },
                                    onClick = {
                                        selectedCondominioId = condominio.id
                                        selectedLabel = condominio.nome
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedCondominioId) },
                enabled = selectedCondominioId.isNotBlank()
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
