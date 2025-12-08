// File name - AppModule.kt
package org.tiffinservice.app.di

import createDataStore
import org.koin.dsl.module
import org.tiffinservice.app.UserPreferencesRepository
import org.tiffinservice.app.database.AppDatabase
import org.tiffinservice.app.repository.CartRepository
import org.tiffinservice.app.repository.MenuRepository
import org.tiffinservice.app.repository.TiffinRepository
import org.tiffinservice.app.viewmodel.CartViewModel
import org.tiffinservice.app.viewmodel.MenuViewModel
import org.tiffinservice.app.viewmodel.OrderPlacedViewModel
import org.tiffinservice.app.viewmodel.TiffinViewModel

val appModule = module {

    single { UserPreferencesRepository(get()) }
    single { get<AppDatabase>().orderDao() }

    single { CartRepository() }
    single { MenuRepository() }
    single { TiffinRepository(get(),get(), get()) }

    single { CartViewModel(get()) }
    single { MenuViewModel(get()) }
    single { TiffinViewModel(get()) }
    single { OrderPlacedViewModel(get()) }
}
