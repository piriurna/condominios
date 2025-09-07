package com.zalamena.condominios.common.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

@OptIn(ExperimentalTime::class)
fun LocalDate.Companion.now(): LocalDate {
    return LocalDateTime.now().date
}

@OptIn(ExperimentalTime::class)
fun LocalTime.Companion.now(): LocalTime {
    return LocalDateTime.now().time
}