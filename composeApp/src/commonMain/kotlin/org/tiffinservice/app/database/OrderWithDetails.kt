package org.tiffinservice.app.database

data class OrderWithDetails(
    val order: OrderEntity,
    val items: List<OrderItemEntity>,
    val foods: List<FoodEntity>
)