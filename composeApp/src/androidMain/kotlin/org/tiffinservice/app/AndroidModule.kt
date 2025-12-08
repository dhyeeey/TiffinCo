package org.tiffinservice.app

import org.koin.dsl.module
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import createDataStore
import org.tiffinservice.app.database.AppDatabase
import org.tiffinservice.app.database.createDatabase

val androidModule = module {

    single<androidx.datastore.core.DataStore<Preferences>> {
        val context = get<Context>()
        createDataStore(context) // <-- the Android overload
    }

    single<AppDatabase> {
        val context = get<Context>()
        createDatabase(context)   // create Room DB
    }

    // DAO bindings
    single { get<AppDatabase>().restaurantDao() }
    single { get<AppDatabase>().foodDao() }
}
