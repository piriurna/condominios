package com.zalamena.condominios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zalamena.condominios.moradores.ui.list.MoradoresListScreen
import com.zalamena.condominios.moradores.ui.list.MoradoresListViewModel
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.repository.MoradoresRepositoryImpl
import com.zalamena.moradores.domain.usecase.GetMoradorUseCase
import com.zalamena.moradores.domain.usecase.GetMoradoresUseCase
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import condominios.composeapp.generated.resources.Res
import condominios.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
    }
}