package org.tiffinservice.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.tiffinservice.app.di.initKoin

fun main() = application {
    if (GlobalContext.getOrNull() == null) {
        initKoin()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "TiffinCo",
    ) {
        App()
    }
}