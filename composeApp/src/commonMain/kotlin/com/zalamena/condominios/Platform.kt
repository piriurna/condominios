package com.zalamena.condominios

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform