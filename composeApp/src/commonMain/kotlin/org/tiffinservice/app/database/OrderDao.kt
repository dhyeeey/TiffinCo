package org.tiffinservice.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    suspend fun getOrders(): List<OrderWithItems>

    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :id LIMIT 1")
    suspend fun getOrder(id: Long): OrderWithItems?

    @Query("UPDATE orders SET status = :status WHERE orderId = :id")
    suspend fun updateStatus(id: Long, status: String)
}
