// FileName : AppDatabase.kt commomMain
package org.tiffinservice.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RestaurantEntity::class,
        FoodEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun foodDao(): FoodDao

    abstract fun orderDao(): OrderDao
}
