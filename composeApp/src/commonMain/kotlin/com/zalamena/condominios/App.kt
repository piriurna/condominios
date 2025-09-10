package com.zalamena.condominios

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.zalamena.condominios.moradores.ui.list.MoradoresListScreen
import com.zalamena.condominios.moradores.ui.list.MoradoresListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<MoradoresListViewModel>()
    MaterialTheme {
        LaunchedEffect(Unit) {
            viewModel.getMoradores()
        }

        MoradoresListScreen(viewModel)
    }
}