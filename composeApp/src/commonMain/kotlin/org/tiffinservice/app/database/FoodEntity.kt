package org.tiffinservice.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "foods",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["restaurant_id"],
            childColumns = ["restaurant_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("restaurant_id")]
)
data class FoodEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val food_id: Long = 0L,
    val restaurant_id: Long,
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val cuisineType: String? = null,
    val price: Double,
    val imageUrl: String? = null,
    val rating: Double = 0.0,
    val isAvailable: Boolean = true,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)
