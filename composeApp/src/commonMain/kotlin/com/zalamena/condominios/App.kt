package com.zalamena.condominios

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.zalamena.condominios.apartamentos.ui.add.AddApartamentoScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
//        MoradoresListScreen(koinViewModel())
//        AddMoradorScreen(koinViewModel())
        AddApartamentoScreen(koinViewModel())
    }
}