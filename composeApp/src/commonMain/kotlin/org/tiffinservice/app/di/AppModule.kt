// File name - AppModule.kt
package org.tiffinservice.app.di

import org.koin.dsl.module
import org.tiffinservice.app.repository.CartRepository
import org.tiffinservice.app.repository.MenuRepository
import org.tiffinservice.app.viewmodel.CartViewModel
import org.tiffinservice.app.viewmodel.MenuViewModel

val appModule = module {
    single { CartRepository() }
    single { MenuRepository() }

    single { CartViewModel(get()) }
    single { MenuViewModel(get()) }
}
