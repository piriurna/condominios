package com.zalamena.condominios.condominio.ui.addmorador.searchpessoa

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPessoaScreen(
    viewModel: SearchPessoaViewModel,
    onNavigateToOverview: (pessoaId: String) -> Unit = {},
    onNavigateToCreatePessoa: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPessoas()
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is SearchPessoaNavigationEvent.SelectPessoa -> {
                onNavigateToOverview(event.pessoaId)
                viewModel.onNavigationHandled()
            }
            is SearchPessoaNavigationEvent.CreateNewPessoa -> {
                onNavigateToCreatePessoa()
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Pessoa") },
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
                label = { Text("CPF ou nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    uiState.isError -> {
                        Text(
                            text = "Erro ao carregar pessoas",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    uiState.pessoas.isEmpty() && uiState.query.isNotBlank() -> {
                        EmptyState(message = "Nenhuma pessoa encontrada")
                    }
                    uiState.pessoas.isEmpty() -> {
                        EmptyState(message = "Busque por CPF ou nome")
                    }
                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.pessoas) { pessoa ->
                                PessoaSearchCard(
                                    pessoa = pessoa,
                                    onClick = { viewModel.onPessoaClick(pessoa.pessoaId) }
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.onCreateNewPessoaClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
            ) {
                Text("Criar nova pessoa")
            }
        }
    }
}

@Composable
private fun PessoaSearchCard(pessoa: SearchPessoaUiData, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(pessoa.nome, style = MaterialTheme.typography.titleMedium)
            Text(pessoa.maskedCpf, style = MaterialTheme.typography.bodySmall)
        }
    }
}
