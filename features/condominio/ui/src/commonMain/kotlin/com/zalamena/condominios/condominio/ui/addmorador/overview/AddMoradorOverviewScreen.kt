package com.zalamena.condominios.condominio.ui.addmorador.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zalamena.condominios.common.ui.components.loading.FullscreenLoading
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.ui.moradores.models.SelectApartamentoUiData
import com.zalamena.condominios.condominio.ui.moradores.models.SelectPessoaUiData

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddMoradorOverviewScreen(
    viewModel: AddMoradorOverviewViewModel,
    pessoaId: String?,
    apartamentoId: String?,
    navigate: () -> Unit = {}
) {
    val uiState = viewModel.uiState.value
    LaunchedEffect(Unit) {
        viewModel.populateForm(
            pessoaId,
            apartamentoId
        )
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            is AddMoradorOverviewNavEvent.Completed -> {
                navigate()
                viewModel.onNavigationHandled()
            }
            null -> {}
        }
    }

    when {
        uiState.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error)
            }
        }

        uiState.isLoading -> {
            FullscreenLoading(isLoading = uiState.isLoading)
        }

        else -> {
            AddMoradorOverviewScreenContent(
                uiState,
                onTipoSelected = viewModel::setTipo,
                onCompleteClicked = viewModel::addMorador
            )
        }

    }
}

@Composable
private fun AddMoradorOverviewScreenContent(
    uiState: AddMoradorOverviewUiState,
    onTipoSelected: (MoradorTipo) -> Unit,
    onCompleteClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),

    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(uiState.pessoa != null) {
                PessoaSection(uiState.pessoa)
            }
            HorizontalDivider(Modifier.fillMaxWidth())
            if(uiState.apartamento != null) {
                ApartamentoSection(uiState.apartamento)
            }
            HorizontalDivider(Modifier.fillMaxWidth())
            TipoSection(
                selectedTipo = uiState.selectedTipo,
                onTipoSelected = onTipoSelected
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 62.dp)
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 32.dp),
            onClick = onCompleteClicked
        ) {
            Text("Concluir Processo")
        }
    }
}

@Composable
private fun TipoSection(
    selectedTipo: MoradorTipo,
    onTipoSelected: (MoradorTipo) -> Unit
) {
    val options = listOf(
        MoradorTipo.PROPRIETARIO to "Proprietário",
        MoradorTipo.RESIDENTE to "Morador",
        MoradorTipo.RESIDENTE_TEMPORARIO to "Temporário"
    )
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text("Tipo:", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        options.forEach { (tipo, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = selectedTipo == tipo,
                    onClick = { onTipoSelected(tipo) }
                )
                Text(label, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun PessoaSection(
    pessoaUiData: SelectPessoaUiData
) {
    Column(
        Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quem:", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))
        Text("ID: ${pessoaUiData.id}")
        Spacer(Modifier.height(16.dp))
        Text("Nome: ${pessoaUiData.nome}")
        pessoaUiData.cpf?.let {
            Spacer(Modifier.height(16.dp))
            Text("CPF: ${pessoaUiData.cpf}")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ApartamentoSection(
    apartamentoUiData: SelectApartamentoUiData
) {
    Column(
        Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(10.dp))
        Text("Onde:", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))
        Text("ID: ${apartamentoUiData.id}")
        Spacer(Modifier.height(16.dp))
        Text("Apartamento: ${apartamentoUiData.numero}")
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun AddMoradorOverviewScreenPreview() {
    AddMoradorOverviewScreenContent(
        AddMoradorOverviewUiState(
            pessoa = SelectPessoaUiData.dummy.copy(id = "JS-12345678900"),
            apartamento = SelectApartamentoUiData.dummy.copy(id = "PDT-703-7")
        ),
        onTipoSelected = {},
        onCompleteClicked = {}
    )
}