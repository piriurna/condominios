package com.zalamena.condominios.condominio.ui.moradores.search

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.EmptyState
import com.zalamena.condominios.common.ui.components.MoradorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoradorSearchScreen(
    viewModel: MoradorSearchViewModel,
    onNavigateToMoradorDetail: (pessoaId: String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMoradores()
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is MoradorSearchNavigationEvent.MoradorDetail -> {
                onNavigateToMoradorDetail(event.pessoaId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Morador") },
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
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::setQuery,
                label = { Text("Nome ou apartamento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    uiState.isError -> {
                        Text(
                            text = "Erro ao carregar moradores",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    uiState.moradores.isEmpty() && uiState.query.isNotBlank() -> {
                        EmptyState(
                            message = "Nenhum morador encontrado",
                            icon = Icons.Default.Search
                        )
                    }
                    uiState.moradores.isEmpty() -> {
                        EmptyState(
                            message = "Busque por nome ou numero do apartamento",
                            icon = Icons.Default.Search
                        )
                    }
                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.moradores) { morador ->
                                MoradorSearchCard(
                                    morador = morador,
                                    onClick = { viewModel.onMoradorClick(morador.pessoaId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoradorSearchCard(morador: MoradorSearchUiData, onClick: () -> Unit) {
    MoradorCard(
        nome = "${morador.nome} - Apt ${morador.aptNumero}",
        cpf = morador.maskedCpf,
        tipoLabel = morador.tipoLabel,
        onClick = onClick
    )
}
