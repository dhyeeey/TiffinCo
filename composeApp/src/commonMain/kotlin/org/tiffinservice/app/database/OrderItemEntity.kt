package org.tiffinservice.app.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "order_items",
    primaryKeys = ["orderId", "foodId"],

    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["food_id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],

    indices = [
        Index("orderId"),
        Index("foodId")
    ]
)
data class OrderItemEntity(
    val orderId: Long,
    val foodId: Long,
    val quantity: Int,
    val price: Double                // snapshot price
)
