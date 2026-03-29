package com.zalamena.condominios.condominio.ui.moradores.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.ui.apartamento.detail.models.toLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoradorInfoScreen(
    viewModel: MoradorInfoViewModel,
    isAdminMode: Boolean = false,
    onNavigateToApartamento: (apartamentoId: String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is MoradorInfoNavigationEvent.ApartamentoDetails -> {
                onNavigateToApartamento(event.apartamentoId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    if (uiState.showCreateAccountDialog) {
        CreateAccountDialog(
            email = uiState.createAccountEmail,
            password = uiState.createAccountPassword,
            emailError = uiState.createAccountEmailError,
            passwordError = uiState.createAccountPasswordError,
            generalError = uiState.createAccountError,
            isLoading = uiState.isCreatingAccount,
            onEmailChanged = viewModel::onCreateAccountEmailChanged,
            onPasswordChanged = viewModel::onCreateAccountPasswordChanged,
            onConfirm = viewModel::onCreateAccount,
            onDismiss = viewModel::onDismissCreateAccountDialog
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Morador") },
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
        Box(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.isError -> {
                    Text(
                        text = "Erro ao carregar morador",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    MoradorDetailContent(
                        uiState = uiState,
                        isAdminMode = isAdminMode,
                        onApartamentoClick = viewModel::onApartamentoClick,
                        onSaveTipo = viewModel::onSaveTipo,
                        onCreateAccount = viewModel::onShowCreateAccountDialog
                    )
                }
            }
        }
    }
}

@Composable
private fun MoradorDetailContent(
    uiState: MoradorDetailUiState,
    isAdminMode: Boolean,
    onApartamentoClick: (apartamentoId: String) -> Unit,
    onSaveTipo: (apartamentoId: String, newTipo: MoradorTipo) -> Unit,
    onCreateAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(uiState.nome, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(uiState.maskedCpf, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(8.dp))
            Text(uiState.email, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Phone,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(8.dp))
            Text(uiState.telefone, style = MaterialTheme.typography.bodyMedium)
        }

        // Account section (admin mode only)
        if (isAdminMode) {
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            Text("Conta de Acesso", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (uiState.accountEmail != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "Conta ativa",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                uiState.accountEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = onCreateAccount,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Criar conta de acesso")
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        Text("Apartamentos", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (uiState.tipoError != null) {
            Text(
                text = uiState.tipoError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(8.dp))
        }

        if (uiState.apartamentos.isEmpty()) {
            Text("Nenhum apartamento vinculado.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.apartamentos) { apt ->
                    ApartamentoDoMoradorCard(
                        apt = apt,
                        isAdminMode = isAdminMode,
                        onClick = { onApartamentoClick(apt.apartamentoId) },
                        onSaveTipo = { newTipo -> onSaveTipo(apt.apartamentoId, newTipo) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateAccountDialog(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    generalError: String?,
    isLoading: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Criar conta de acesso") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Informe o email e uma senha temporaria para o morador.",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChanged,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = { emailError?.let { Text(it) } },
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChanged,
                    label = { Text("Senha temporaria") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = passwordError != null,
                    supportingText = { passwordError?.let { Text(it) } },
                    enabled = !isLoading
                )

                generalError?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text("Criar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApartamentoDoMoradorCard(
    apt: ApartamentoDoMoradorUiData,
    isAdminMode: Boolean,
    onClick: () -> Unit,
    onSaveTipo: (MoradorTipo) -> Unit
) {
    var selectedTipo by remember(apt.apartamentoId, apt.tipo) { mutableStateOf(apt.tipo) }
    var expanded by remember { mutableStateOf(false) }
    val hasChanged = selectedTipo != apt.tipo

    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.MeetingRoom,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Apt ${apt.numero}", style = MaterialTheme.typography.titleSmall)
                        Text("Andar ${apt.andar}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (!isAdminMode) {
                    Text(
                        text = apt.tipoLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (isAdminMode) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedTipo.toLabel(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            MoradorTipo.entries.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.toLabel()) },
                                    onClick = {
                                        selectedTipo = tipo
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    if (hasChanged) {
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { onSaveTipo(selectedTipo) }) {
                            Text("Salvar")
                        }
                    }
                }
            }
        }
    }
}
