package com.zalamena.condominios.condominio.ui.addcondominio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading
import com.zalamena.condominios.condominio.domain.address.Cidade
import com.zalamena.condominios.condominio.domain.address.Estado

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

@OptIn(ExperimentalMaterial3Api::class)
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
        Text("Dados do condominio", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.form.nome,
            label = { Text("Nome") },
            onValueChange = onNomeChange,
            singleLine = true,
            isError = uiState.nomeError != null,
            supportingText = { uiState.nomeError?.let { Text(it) } }
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.form.cep,
            label = { Text("CEP") },
            onValueChange = onCepChange,
            singleLine = true,
            isError = uiState.cepError != null,
            supportingText = {
                when {
                    uiState.cepError != null -> Text(uiState.cepError)
                    uiState.cepMessage != null -> Text(uiState.cepMessage)
                }
            },
            trailingIcon = {
                if (uiState.isLoadingCep) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(3f),
                value = uiState.form.rua,
                label = { Text("Rua") },
                onValueChange = onRuaChange,
                singleLine = true,
                isError = uiState.ruaError != null,
                supportingText = { uiState.ruaError?.let { Text(it) } }
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                modifier = Modifier.weight(1f),
                value = uiState.form.numero,
                label = { Text("Numero") },
                onValueChange = onNumeroChange,
                singleLine = true,
                isError = uiState.numeroError != null,
                supportingText = { uiState.numeroError?.let { Text(it) } }
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            if (uiState.useDropdowns) {
                SearchableDropdown(
                    modifier = Modifier.weight(3f),
                    label = "Cidade",
                    value = uiState.form.cidade,
                    options = uiState.cidades.map { it.nome },
                    isLoading = uiState.isLoadingCidades,
                    isError = uiState.cidadeError != null,
                    errorText = uiState.cidadeError,
                    onValueChange = onCidadeChange
                )
            } else {
                TextField(
                    modifier = Modifier.weight(3f),
                    value = uiState.form.cidade,
                    label = { Text("Cidade") },
                    onValueChange = onCidadeChange,
                    singleLine = true,
                    isError = uiState.cidadeError != null,
                    supportingText = { uiState.cidadeError?.let { Text(it) } }
                )
            }
            Spacer(Modifier.width(8.dp))
            if (uiState.useDropdowns) {
                EstadoDropdown(
                    modifier = Modifier.weight(1f),
                    value = uiState.form.estado,
                    estados = uiState.estados,
                    isLoading = uiState.isLoadingEstados,
                    isError = uiState.estadoError != null,
                    errorText = uiState.estadoError,
                    onEstadoSelected = onEstadoChange
                )
            } else {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.form.estado,
                    label = { Text("Estado") },
                    onValueChange = onEstadoChange,
                    singleLine = true,
                    isError = uiState.estadoError != null,
                    supportingText = { uiState.estadoError?.let { Text(it) } }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddClick, modifier = Modifier.fillMaxWidth()) {
            Text("Adicionar condominio")
        }
        Spacer(Modifier.height(8.dp))
        Text(uiState.errorMessage ?: "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstadoDropdown(
    modifier: Modifier = Modifier,
    value: String,
    estados: List<Estado>,
    isLoading: Boolean,
    isError: Boolean,
    errorText: String?,
    onEstadoSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            value = value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("UF") },
            isError = isError,
            supportingText = { errorText?.let { Text(it) } },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            estados.forEach { estado ->
                DropdownMenuItem(
                    text = { Text("${estado.sigla} - ${estado.nome}") },
                    onClick = {
                        onEstadoSelected(estado.sigla)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchableDropdown(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    options: List<String>,
    isLoading: Boolean,
    isError: Boolean,
    errorText: String?,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredOptions = remember(options, searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else options.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                searchQuery = newValue
                expanded = true
            },
            singleLine = true,
            label = { Text(label) },
            isError = isError,
            supportingText = { errorText?.let { Text(it) } },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        )
        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.take(50).forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            searchQuery = ""
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
