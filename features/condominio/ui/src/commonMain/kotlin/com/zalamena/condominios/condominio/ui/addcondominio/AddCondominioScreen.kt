package com.zalamena.condominios.condominio.ui.addcondominio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading

@Composable
fun AddCondominioScreen(
    viewModel: AddCondominioViewModel,
    navigate: suspend () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.value.createdCondominioId) {
        if (uiState.value.createdCondominioId != null) navigate()
    }

    AddCondominioContent(
        uiState = uiState.value,
        onNomeChange = viewModel::setNome,
        onRuaChange = viewModel::setRua,
        onNumeroChange = viewModel::setNumero,
        onCepChange = viewModel::setCep,
        onCidadeChange = viewModel::setCidade,
        onEstadoChange = viewModel::setEstado,
        onAddClick = viewModel::addCondominio
    )

    FullscreenLoading(isLoading = uiState.value.isLoading)
}

@Composable
private fun AddCondominioContent(
    uiState: AddCondominioUiState,
    onNomeChange: (String) -> Unit = {},
    onRuaChange: (String) -> Unit = {},
    onNumeroChange: (String) -> Unit = {},
    onCepChange: (String) -> Unit = {},
    onCidadeChange: (String) -> Unit = {},
    onEstadoChange: (String) -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dados do condomínio", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.form.nome,
            label = { Text("Nome") },
            onValueChange = onNomeChange,
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.form.rua,
            label = { Text("Rua") },
            onValueChange = onRuaChange,
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(2f),
                value = uiState.form.numero,
                label = { Text("Número") },
                onValueChange = onNumeroChange,
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                modifier = Modifier.weight(3f),
                value = uiState.form.cep,
                label = { Text("CEP") },
                onValueChange = onCepChange,
                singleLine = true
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(3f),
                value = uiState.form.cidade,
                label = { Text("Cidade") },
                onValueChange = onCidadeChange,
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                modifier = Modifier.weight(1f),
                value = uiState.form.estado,
                label = { Text("Estado") },
                onValueChange = onEstadoChange,
                singleLine = true
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddClick, modifier = Modifier.fillMaxWidth()) {
            Text("Adicionar condomínio")
        }
        Spacer(Modifier.height(8.dp))
        Text(uiState.errorMessage ?: "")
    }
}
