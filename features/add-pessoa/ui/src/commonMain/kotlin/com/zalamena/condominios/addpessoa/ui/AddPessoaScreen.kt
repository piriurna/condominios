package com.zalamena.condominios.addpessoa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaFormError
import com.zalamena.condominios.addpessoa.ui.models.AddPessoaFormUiData
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddPessoaScreen(
    viewModel: AddPessoaViewModel
) {
    val uiState = viewModel.uiState

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
        Text("Dados da pessoa", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = uiState.addPessoaForm.nome,
                label = { Text("Nome Completo") },
                isError = uiState.formErrors.contains(AddPessoaFormError.Nome),
                supportingText = {
                    if (uiState.formErrors.contains(AddPessoaFormError.Nome)) {
                        Text("Nome inválido")
                    }
                },
                onValueChange = {
                    onFormChange(uiState.addPessoaForm.copy(nome = it))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = uiState.addPessoaForm.email,
                label = { Text("E-mail") },
                isError = uiState.formErrors.contains(AddPessoaFormError.Email),
                supportingText = {
                    if (uiState.formErrors.contains(AddPessoaFormError.Email)) {
                        Text("Email inválido")
                    }
                },
                onValueChange = {
                    onFormChange(uiState.addPessoaForm.copy(email = it))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),

            )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = uiState.addPessoaForm.email,
            label = { Text("Telefone") },
            isError = uiState.formErrors.contains(AddPessoaFormError.Telefone),
            supportingText = {
                if (uiState.formErrors.contains(AddPessoaFormError.Telefone)) {
                    Text("Telefone inválido")
                }
            },
            onValueChange = {
                onFormChange(uiState.addPessoaForm.copy(telefone = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = uiState.addPessoaForm.email,
            label = { Text("Cpf") },
            isError = uiState.formErrors.contains(AddPessoaFormError.Cpf),
            supportingText = {
                if (uiState.formErrors.contains(AddPessoaFormError.Cpf)) {
                    Text("CPF inválido")
                }
            },
            onValueChange = {
                onFormChange(uiState.addPessoaForm.copy(telefone = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
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