package com.zalamena.condominios.moradores.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.zalamena.condominios.moradores.ui.models.SelectApartamentoUiData
import com.zalamena.condominios.moradores.ui.models.SelectPessoaUiData
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
        AddMoradorFormDropdown(
            items = uiState.pessoasList,
            selectedItem = uiState.pessoaSelected ,
            onSelect = onPessoaSelected,
            itemText = { it.nome },
            hint = "Selecione a pessoa"
        )
        Spacer(Modifier.height(16.dp))
        AddMoradorFormDropdown(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> AddMoradorFormDropdown(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    itemText: (T) -> String,
    hint: String = "Select an option"
) {
    var dropdownState by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = dropdownState,
        onExpandedChange = {
            dropdownState = !dropdownState
        },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            value = selectedItem?.let { itemText(it) }?:"",
            label = { Text(hint) },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownState)
            }
        )
        ExposedDropdownMenu(
            expanded = dropdownState,
            onDismissRequest = { dropdownState = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(itemText(it))
                    },
                    onClick = {
                        onSelect(it)
                        dropdownState = false
                    }
                )
            }
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