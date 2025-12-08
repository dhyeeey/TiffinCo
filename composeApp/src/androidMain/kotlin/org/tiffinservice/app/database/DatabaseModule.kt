// FileName : DatabaseModule.kt
package org.tiffinservice.app.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun createDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("tiffin_database.db")
    return Room.databaseBuilder<AppDatabase>(
        context=context.applicationContext,
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver()).build()
}
