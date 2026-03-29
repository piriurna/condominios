package com.zalamena.condominios.condominio.ui.apartamento.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.zalamena.condominios.common.ui.components.EmptyState
import com.zalamena.condominios.common.ui.components.MoradorCard
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.MoradorDetailUiData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartamentoDetailScreen(
    viewModel: ApartamentoDetailViewModel,
    isAdminMode: Boolean = true,
    onNavigateToAddMorador: (apartamentoId: String) -> Unit = {},
    onNavigateToMoradorDetail: (pessoaId: String) -> Unit = {},
    onBack: () -> Unit = {}
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState.numero.isNotBlank()) {
                        Text("Apartamento ${uiState.numero}")
                    } else {
                        Text("Apartamento")
                    }
                },
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.isError -> {
                    Text(
                        text = "Erro ao carregar apartamento",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    ApartamentoDetailContent(
                        uiState = uiState,
                        isAdminMode = isAdminMode,
                        onAddMoradorClick = viewModel::onAddMoradorClick,
                        onMoradorClick = viewModel::onMoradorClick,
                        onDeleteMoradorClick = viewModel::onDeleteMoradorClick
                    )
                }
            }
        }
    }

    uiState.showDeleteConfirmation?.let { morador ->
        DeleteMoradorConfirmationDialog(
            moradorName = morador.nome,
            onConfirm = { viewModel.onConfirmDeleteMorador() },
            onDismiss = { viewModel.onDismissDeleteMorador() }
        )
    }
}

@Composable
private fun ApartamentoDetailContent(
    uiState: ApartamentoDetailUiState,
    isAdminMode: Boolean = true,
    onAddMoradorClick: () -> Unit = {},
    onMoradorClick: (pessoaId: String) -> Unit = {},
    onDeleteMoradorClick: (MoradorDetailUiData) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Andar ${uiState.andar}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Text("Moradores", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (uiState.moradores.isEmpty()) {
            EmptyState(
                message = "Nenhum morador neste apartamento",
                icon = Icons.Default.Person,
                actionLabel = if (isAdminMode) "Adicionar morador" else null,
                onAction = if (isAdminMode) onAddMoradorClick else null
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.moradores) { morador ->
                    MoradorCard(
                        nome = morador.nome,
                        cpf = morador.maskedCpf,
                        tipoLabel = morador.tipoLabel,
                        onClick = { onMoradorClick(morador.pessoaId) },
                        onDelete = if (isAdminMode) {
                            { onDeleteMoradorClick(morador) }
                        } else null
                    )
                }
            }

            if (isAdminMode) {
                Spacer(Modifier.height(16.dp))
                Button(onClick = onAddMoradorClick, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Adicionar Morador")
                }
            }
        }
    }
}

@Composable
private fun DeleteMoradorConfirmationDialog(
    moradorName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir morador") },
        text = { Text("Tem certeza que deseja excluir o morador $moradorName?") },
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
