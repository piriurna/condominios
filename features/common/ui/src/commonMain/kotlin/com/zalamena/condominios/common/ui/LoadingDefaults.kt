package com.zalamena.condominios.common.ui

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

private const val MIN_LOADING_MS = 250L

suspend fun <T> withMinLoading(
    showSpinner: Boolean,
    block: suspend () -> T
): T = coroutineScope {
    val minDelay = if (showSpinner) async { delay(MIN_LOADING_MS) } else null
    val result = block()
    minDelay?.await()
    result
}
