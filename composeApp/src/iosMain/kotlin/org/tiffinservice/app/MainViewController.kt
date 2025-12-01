package org.tiffinservice.app

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.tiffinservice.app.database.createDatabase
import org.tiffinservice.app.di.appModule

fun MainViewController() = ComposeUIViewController {
    runCatching {
        startKoin { modules(appModule) }
    }.onFailure {
        // Ignore "already started" exceptions
    }

//    val appDatabase = remember {
//        createDatabase()
//    }

    App()
}