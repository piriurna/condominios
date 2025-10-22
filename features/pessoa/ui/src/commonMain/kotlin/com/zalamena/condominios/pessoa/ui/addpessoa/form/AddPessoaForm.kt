package com.zalamena.condominios.pessoa.ui.addpessoa.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaFormError
import com.zalamena.condominios.pessoa.ui.addpessoa.models.AddPessoaFormUiData

@Composable
fun AddPessoaForm(
    modifier: Modifier = Modifier,
    addPessoaForm: AddPessoaFormUiData,
    onFormChange: (AddPessoaFormUiData) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dados da pessoa", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = addPessoaForm.nome,
            label = { Text("Nome Completo") },
            isError = addPessoaForm.formErrors.contains(AddPessoaFormError.Nome),
            supportingText = {
                if (addPessoaForm.formErrors.contains(AddPessoaFormError.Nome)) {
                    Text("Nome inválido")
                }
            },
            onValueChange = {
                onFormChange(addPessoaForm.copy(nome = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = addPessoaForm.email,
            label = { Text("E-mail") },
            isError = addPessoaForm.formErrors.contains(AddPessoaFormError.Email),
            supportingText = {
                if (addPessoaForm.formErrors.contains(AddPessoaFormError.Email)) {
                    Text("Email inválido")
                }
            },
            onValueChange = {
                onFormChange(addPessoaForm.copy(email = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),

            )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = addPessoaForm.telefone,
            label = { Text("Telefone") },
            isError = addPessoaForm.formErrors.contains(AddPessoaFormError.Telefone),
            supportingText = {
                if (addPessoaForm.formErrors.contains(AddPessoaFormError.Telefone)) {
                    Text("Telefone inválido")
                }
            },
            onValueChange = {
                onFormChange(addPessoaForm.copy(telefone = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = addPessoaForm.cpf,
            label = { Text("Cpf") },
            isError = addPessoaForm.formErrors.contains(AddPessoaFormError.Cpf),
            supportingText = {
                if (addPessoaForm.formErrors.contains(AddPessoaFormError.Cpf)) {
                    Text("CPF inválido")
                }
            },
            onValueChange = {
                onFormChange(addPessoaForm.copy(cpf = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
        )
    }
}