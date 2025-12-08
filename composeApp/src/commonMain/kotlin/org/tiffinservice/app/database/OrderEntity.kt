package org.tiffinservice.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "orders")
data class OrderEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0L,

    val userId: Long,                             // if needed, else remove

    val totalAmount: Double,
    val deliveryFee: Double,
    val discount: Double = 0.0,

    val address: String,                          // final full address string
    val status: String = "PENDING",               // PENDING, CONFIRMED, COMPLETED, CANCELED

    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)
