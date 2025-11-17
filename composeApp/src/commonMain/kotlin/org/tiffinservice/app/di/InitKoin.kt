// File name - InitKoin.kt
package org.tiffinservice.app.di

import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(appModule)
}
