package com.zalamena.condominios

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.zalamena.condominios.di.PlatformKoinInitializer

fun main() = application {

    PlatformKoinInitializer.init()
    Window(
        onCloseRequest = ::exitApplication,
        title = "condominios",
    ) {
        App()
    }
}