// File name - AppModule.kt
package org.tiffinservice.app.di

import org.koin.dsl.module
import org.tiffinservice.app.repository.CartRepository
import org.tiffinservice.app.repository.MenuRepository
import org.tiffinservice.app.repository.TiffinRepository
import org.tiffinservice.app.viewmodel.CartViewModel
import org.tiffinservice.app.viewmodel.MenuViewModel
import org.tiffinservice.app.viewmodel.TiffinViewModel

val appModule = module {
    single { CartRepository() }
    single { MenuRepository() }
    single { TiffinRepository() }

    single { CartViewModel(get()) }
    single { MenuViewModel(get()) }
    single { TiffinViewModel(get()) }
}
