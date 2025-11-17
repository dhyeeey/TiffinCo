package org.tiffinservice.app.ui

import androidx.compose.ui.graphics.Color
import org.tiffinservice.app.getPlatform

val OrangeMain = Color(0xFFF7931E)
val DarkBackground = Color(0xFF1A2332)
val LightBackground = Color(0xFFFFF8F0)
val TextBrown = Color(0xFF8B6F47)
val Primary = Color(0xFFF48C25)
val Background = Color(0xFFFFF8F0)
val TextGray = Color(0xFF666666)

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}