// DatabaseModule.kt
package org.tiffinservice.app.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createDatabase(): AppDatabase {

    val paths = NSSearchPathForDirectoriesInDomains(
        directory = NSApplicationSupportDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    )

    val dir = paths.firstOrNull() as? String ?: NSHomeDirectory()

    NSFileManager.defaultManager.createDirectoryAtPath(
        path = dir,
        withIntermediateDirectories = true,
        attributes = null,
        error = null
    )

    val dbFile = "$dir/tiffin_database.db"

    return Room.databaseBuilder<AppDatabase>(
        name = dbFile,
        factory = { AppDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
