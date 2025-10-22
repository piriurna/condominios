package com.zalamena.condominios.condominio.ui.moradores.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zalamena.condominios.common.ui.components.form.AppExposedDropdownMenuBox
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading
import com.zalamena.condominios.condominio.ui.moradores.models.SelectApartamentoUiData
import com.zalamena.condominios.condominio.ui.moradores.models.SelectPessoaUiData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddMoradorScreen(
    viewModel: AddMoradorViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState(AddMoradorUiState())



    LaunchedEffect(Unit) {
        viewModel.populateForm()
    }

    AddMoradorScreenContent(
        uiState.value,
        viewModel::pessoaSelected,
        viewModel::apartamentoSelected,
        viewModel::addMorador
    )

    FullscreenLoading(isLoading = uiState.value.isLoading)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoradorScreenContent(
    uiState: AddMoradorUiState,
    onPessoaSelected: (SelectPessoaUiData) -> Unit,
    onApartamentoSelected: (SelectApartamentoUiData) -> Unit,
    onAddMoradorClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppExposedDropdownMenuBox(
            items = uiState.pessoasList,
            selectedItem = uiState.pessoaSelected ,
            onSelect = onPessoaSelected,
            itemText = { it.nome },
            hint = "Selecione a pessoa"
        )
        Spacer(Modifier.height(16.dp))
        AppExposedDropdownMenuBox(
            items = uiState.apartamentosList,
            selectedItem = uiState.apartamentoSelected,
            onSelect = onApartamentoSelected,
            itemText = { it.numero },
            hint = "Selecione o apartamento"
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddMoradorClicked) {
            Text("Add Morador")
        }
    }
}

@Preview
@Composable
fun AddMoradorScreenContentPreview() {
    var selectedIdPessoa by remember { mutableStateOf<SelectPessoaUiData?>(null) }
    var selectedIdApartamento by remember { mutableStateOf<SelectApartamentoUiData?>(null) }

    AddMoradorScreenContent(
        AddMoradorUiState(
            pessoasList = listOf(SelectPessoaUiData.dummy),
            apartamentosList = listOf(SelectApartamentoUiData.dummy),
            pessoaSelected = selectedIdPessoa,
            apartamentoSelected = selectedIdApartamento
        ),
        { selectedIdPessoa = it},
        { selectedIdApartamento = it },
        onAddMoradorClicked = {}
    )
}