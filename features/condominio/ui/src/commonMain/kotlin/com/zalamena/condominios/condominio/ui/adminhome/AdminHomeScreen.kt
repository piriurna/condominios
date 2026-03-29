package com.zalamena.condominios.condominio.ui.adminhome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.CondominioCard
import com.zalamena.condominios.common.ui.components.EmptyState
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
            Button(onClick = onLogout) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Sair")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddCondominioClick() },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar condominio")
            }
        }
    ) {
        if (state.condominios.isEmpty()) {
            EmptyState(
                message = "Nenhum condominio cadastrado",
                icon = Icons.Default.LocationCity,
                actionLabel = "Adicionar condominio",
                onAction = { viewModel.onAddCondominioClick() }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(state.condominios) { condominio ->
                    CondominioCard(
                        nome = condominio.nome,
                        endereco = condominio.enderecoDescription,
                        apartamentoCount = condominio.apartamentoCount,
                        onClick = { viewModel.onCondominioClick(condominio.id) }
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}
