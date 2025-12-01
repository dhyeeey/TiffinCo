package org.tiffinservice.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
@Entity(tableName = "restaurants")
data class RestaurantEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val restaurant_id: Long = 0L,
    val name: String,
    val description: String? = null,
    val address: String,
    val city: String,
    val state: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val websiteUrl: String? = null,
    val thumbnailImageUrl: String? = null,
    val rating: Double = 0.0,
    val isOpen: Boolean = true,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)
