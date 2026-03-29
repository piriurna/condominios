package com.zalamena.login.ui.createuser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    viewModel: CreateUserViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.navigationEvent) {
        when (state.navigationEvent) {
            is CreateUserNavigationEvent.NavigateBack -> {
                onNavigateBack()
                viewModel.onNavigationHandled()
            }
            null -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Usuário") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onNavigateBack() }) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.nameError != null,
                supportingText = { state.nameError?.let { Text(it) } }
            )

            OutlinedTextField(
                value = state.cpf,
                onValueChange = viewModel::setCpf,
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.cpfError != null,
                supportingText = { state.cpfError?.let { Text(it) } }
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::setEmail,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.emailError != null,
                supportingText = { state.emailError?.let { Text(it) } }
            )

            RoleDropdown(
                selectedRole = state.selectedRole,
                onRoleSelected = viewModel::setRole
            )

            if (state.selectedRole == RoleOption.PORTEIRO) {
                OutlinedTextField(
                    value = state.condominioId,
                    onValueChange = viewModel::setCondominioId,
                    label = { Text("ID do Condomínio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = state.condominioIdError != null,
                    supportingText = { state.condominioIdError?.let { Text(it) } }
                )
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::createUser,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.generatedPassword == null
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Criar Usuário")
                }
            }

            state.generatedPassword?.let { password ->
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Usuário criado com sucesso!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Senha gerada:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = password,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Anote esta senha. Ela não será exibida novamente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.onNavigateBack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Voltar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleDropdown(
    selectedRole: RoleOption,
    onRoleSelected: (RoleOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val roleLabel = when (selectedRole) {
        RoleOption.ADMIN -> "Administrador"
        RoleOption.PORTEIRO -> "Porteiro"
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = roleLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Papel") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Administrador") },
                onClick = {
                    onRoleSelected(RoleOption.ADMIN)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Porteiro") },
                onClick = {
                    onRoleSelected(RoleOption.PORTEIRO)
                    expanded = false
                }
            )
        }
    }
}
