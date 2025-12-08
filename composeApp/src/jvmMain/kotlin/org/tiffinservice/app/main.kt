package org.tiffinservice.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.tiffinservice.app.di.appModule
import org.tiffinservice.app.di.initKoin

fun main() = application {
    if (GlobalContext.getOrNull() == null) {
        startKoin {
            modules(appModule)
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "TiffinCo",
    ) {
        App()
    }
}