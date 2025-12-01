package org.tiffinservice.app.database

import androidx.room.*

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long

    @Query("SELECT COUNT(*) FROM restaurants")
    suspend fun countRestaurants(): Int
}
