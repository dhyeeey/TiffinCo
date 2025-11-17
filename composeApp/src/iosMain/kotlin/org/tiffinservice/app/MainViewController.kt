package org.tiffinservice.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.tiffinservice.app.di.appModule

fun MainViewController() = ComposeUIViewController {
    runCatching {
        startKoin { modules(appModule) }
    }.onFailure {
        // Ignore "already started" exceptions
    }

    App()
}