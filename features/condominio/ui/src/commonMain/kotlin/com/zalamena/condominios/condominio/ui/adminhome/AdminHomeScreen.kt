package com.zalamena.condominios.condominio.ui.adminhome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.scaffold.LoadingScaffold

@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel,
    onCondominioClick: (String) -> Unit,
    onAddCondominioClick: () -> Unit,
    onCreateUserClick: () -> Unit = {},
    onLogout: () -> Unit = {}
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
            is AdminHomeNavigationEvent.CreateUser -> {
                onCreateUserClick()
                viewModel.onNavigationHandled()
            }
        }
    }

    LoadingScaffold(
        isLoading = state.isLoading,
        isError = state.isError,
        title = "Condominios",
        errorMessage = "Erro ao carregar condominios",
        actions = {
            Button(onClick = { viewModel.onCreateUserClick() }) {
                Text("Criar Usuário")
            }
            Button(onClick = onLogout) {
                Text("Sair")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddCondominioClick() }) {
                Text("+")
            }
        }
    ) {
        if (state.condominios.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text("Nenhum condominio cadastrado")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.onAddCondominioClick() }) {
                    Text("Criar primeiro condominio")
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.condominios) { condominio ->
                    ListItem(
                        overlineContent = { Text(condominio.id) },
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
