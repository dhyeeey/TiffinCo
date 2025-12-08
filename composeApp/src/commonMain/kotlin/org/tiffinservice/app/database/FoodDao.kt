package org.tiffinservice.app.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFood(foods: List<FoodEntity>): List<Long>

    @Query("SELECT * FROM foods WHERE food_id = :id LIMIT 1")
    suspend fun getFoodById(id: Long): FoodEntity?

    @Query("SELECT * FROM foods WHERE restaurant_id = :restaurantId")
    suspend fun getFoodByRestaurant(restaurantId: Long): List<FoodEntity>

    @Query("SELECT * FROM foods")
    suspend fun getAllFood(): List<FoodEntity>

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun countFood(): Int

    @Query("SELECT * FROM foods WHERE restaurant_id = :restaurantId")
    fun watchFoodsByRestaurant(restaurantId: Long): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE food_id IN (:ids)")
    suspend fun getFoodsByIds(ids: List<Long>): List<FoodEntity>

}