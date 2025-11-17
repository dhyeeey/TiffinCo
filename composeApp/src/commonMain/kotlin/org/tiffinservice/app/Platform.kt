package org.tiffinservice.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform