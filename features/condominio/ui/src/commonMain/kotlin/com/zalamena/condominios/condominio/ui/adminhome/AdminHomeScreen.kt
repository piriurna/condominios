package com.zalamena.condominios.condominio.ui.adminhome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel,
    onCondominioClick: (String) -> Unit,
    onAddCondominioClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    state.navigationEvent?.let { event ->
        when (event) {
            is AdminHomeNavigationEvent.CondominioDetails -> {
                onCondominioClick(event.condominioId)
                viewModel.onNavigationHandled()
            }
            is AdminHomeNavigationEvent.AddCondominio -> {
                onAddCondominioClick()
                viewModel.onNavigationHandled()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Condominios") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddCondominioClick() }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.isError -> {
                    Text(
                        text = "Erro ao carregar condominios",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.condominios.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Nenhum condominio cadastrado")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.onAddCondominioClick() }) {
                            Text("Criar primeiro condominio")
                        }
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.condominios) { condominio ->
                            ListItem(
                                headlineContent = { Text(condominio.nome) },
                                supportingContent = { Text(condominio.enderecoDescription) },
                                modifier = Modifier.clickable {
                                    viewModel.onCondominioClick(condominio.id)
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
