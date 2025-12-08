package org.tiffinservice.app.database

import androidx.room.*

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRestaurants(restaurants: List<RestaurantEntity>): List<Long>

    @Query("SELECT COUNT(*) FROM restaurants")
    suspend fun countRestaurants(): Int

    @Query("SELECT * FROM restaurants ORDER BY name")
    suspend fun getAllRestaurant(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE restaurant_id = :id LIMIT 1")
    suspend fun getRestaurantById(id: Long): RestaurantEntity?
}
