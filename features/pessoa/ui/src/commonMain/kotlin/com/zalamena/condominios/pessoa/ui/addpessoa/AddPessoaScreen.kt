package com.zalamena.condominios.pessoa.ui.addpessoa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading
import com.zalamena.condominios.pessoa.ui.addpessoa.form.AddPessoaForm
import com.zalamena.condominios.pessoa.ui.addpessoa.models.AddPessoaFormUiData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddPessoaScreen(
    viewModel: AddPessoaViewModel,
    navigate: suspend () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState(AddPessoaUiState())

    LaunchedEffect(uiState.value.createdPessoaId) {
        if(uiState.value.createdPessoaId != null) navigate()
    }

    AddPessoaScreenContent(
        uiState.value,
        viewModel::updateForm,
        viewModel::addPessoa
    )

    FullscreenLoading(isLoading = uiState.value.isLoading)
}



@Composable
private fun AddPessoaScreenContent(
    uiState: AddPessoaUiState,
    onFormChange: (AddPessoaFormUiData) -> Unit,
    onAddPessoa: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddPessoaForm(
            modifier = Modifier,
            addPessoaForm = uiState.addPessoaForm,
            onFormChange = onFormChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddPessoa) {
            Text("Adicionar pessoa")
        }
    }
}

@Preview
@Composable
private fun AddPessoaScreenContentPreview() {
    AddPessoaScreenContent(
        AddPessoaUiState(),
        onFormChange = {},
        onAddPessoa = {},
    )
}