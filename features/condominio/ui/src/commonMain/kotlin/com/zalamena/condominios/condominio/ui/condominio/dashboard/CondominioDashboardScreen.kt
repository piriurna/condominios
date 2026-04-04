package com.zalamena.condominios.condominio.ui.condominio.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.zalamena.condominios.common.ui.components.ApartamentoCard
import com.zalamena.condominios.common.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CondominioDashboardScreen(
    viewModel: CondominioDashboardViewModel,
    onNavigateToAddApartamento: (condominioId: String) -> Unit = {},
    onNavigateToApartamento: (apartamentoId: String) -> Unit = {},
    onNavigateToCreatePorteiro: (condominioId: String) -> Unit = {},
    onNavigateToPorteiroList: (condominioId: String) -> Unit = {},
    onNavigateToSearchMorador: (condominioId: String) -> Unit = {},
    onNavigateToAmenityList: (condominioId: String) -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {}
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
            is DashboardNavigationEvent.SearchMorador -> {
                onNavigateToSearchMorador(event.condominioId)
                viewModel.onNavigationHandled()
            }
            is DashboardNavigationEvent.AmenityList -> {
                onNavigateToAmenityList(event.condominioId)
                viewModel.onNavigationHandled()
            }
            is DashboardNavigationEvent.Logout -> {
                onLogout()
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
                navigationIcon = {
                    if (uiState.isAdminMode) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    }
                },
                actions = {
                    if (uiState.isAdminMode) {
                        var menuExpanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Porteiros") },
                                onClick = {
                                    menuExpanded = false
                                    viewModel.onPorteiroListClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Criar Porteiro") },
                                onClick = {
                                    menuExpanded = false
                                    viewModel.onCreatePorteiroClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Comodidades") },
                                onClick = {
                                    menuExpanded = false
                                    viewModel.onAmenityListClick()
                                }
                            )
                        }
                    } else {
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Sair"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.isAdminMode && !uiState.isLoading && !uiState.isError) {
                FloatingActionButton(
                    onClick = { viewModel.onAddApartamentoClick() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar apartamento")
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
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.apartamentos.isEmpty() -> {
                    if (uiState.isAdminMode) {
                        EmptyState(
                            message = "Nenhum apartamento cadastrado",
                            icon = Icons.Default.MeetingRoom,
                            actionLabel = "Adicionar apartamento",
                            onAction = { viewModel.onAddApartamentoClick() }
                        )
                    } else {
                        EmptyState(
                            message = "Nenhum apartamento neste condominio",
                            icon = Icons.Default.MeetingRoom
                        )
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
                            ApartamentoCard(
                                numero = apt.numero,
                                andar = apt.andar,
                                moradorCount = apt.moradorCount,
                                onClick = { viewModel.onApartamentoClick(apt.id) },
                                proprietarioNome = apt.proprietarioNome
                            )
                        }
                    }
                }
            }
        }
    }
}

