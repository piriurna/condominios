package com.zalamena.condominios.condominio.ui.addapartamento

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddApartamentoScreen(
    viewModel: AddApartamentoViewModel,
    navigate: suspend () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState(AddApartamentoUiState())

    LaunchedEffect(viewModel.uiState.value.createdApartamentoId) {
        if(viewModel.uiState.value.createdApartamentoId != null) navigate()
    }

    AddApartamentoScreenContent(
        uiState.value,
        onAndarApartamentoChange = viewModel::setAndarApartamento,
        onNumeroApartamentoChange = viewModel::setNumeroApartamento,
        addApartamentoClick = viewModel::addApartamento
    )

    FullscreenLoading(isLoading = uiState.value.isLoading)
}

@Composable
private fun AddApartamentoScreenContent(
    uiState: AddApartamentoUiState,
    onNumeroApartamentoChange: (String) -> Unit = {},
    onAndarApartamentoChange: (String) -> Unit = {},
    addApartamentoClick: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dados do apartamento", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            TextField(
                modifier = Modifier.weight(2f),
                value = uiState.addApartamentoForm.numero,
                label = { Text("Número") },
                onValueChange = {
                    onNumeroApartamentoChange(it)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = uiState.numeroError != null,
                supportingText = { uiState.numeroError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                modifier = Modifier.weight(1f),
                value = uiState.addApartamentoForm.andar,
                label = { Text("Andar") },
                onValueChange = { onAndarApartamentoChange(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = uiState.andarError != null,
                supportingText = { uiState.andarError?.let { Text(it) } }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = addApartamentoClick) {
            Text("Adicionar apartamento")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(uiState.errorMessage ?: "")
    }
}


@Preview
@Composable
private fun AddApartamentoScreenContentPreview() {
    AddApartamentoScreenContent(
        AddApartamentoUiState(),
        onNumeroApartamentoChange = {},
        onAndarApartamentoChange = {}
    )
}