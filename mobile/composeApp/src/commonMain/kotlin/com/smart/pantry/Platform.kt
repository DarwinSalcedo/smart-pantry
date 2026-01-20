package com.smart.pantry

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform