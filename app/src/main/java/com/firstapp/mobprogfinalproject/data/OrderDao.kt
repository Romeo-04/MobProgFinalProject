
package com.firstapp.mobprogfinalproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    // Function for Andrei (Menu, Cart, and Checkout)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long // Returns the new orderId

    // Function for Andrei (Menu, Cart, and Checkout)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    // Function for Marcus (Orders Tracking)
    @Query("SELECT * FROM orders WHERE customerId = :userId ORDER BY orderDate DESC")
    fun getOrdersForUser(userId: Int): Flow<List<Order>>

    // Function for Marcus (Orders Tracking)
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItems(orderId: Int): List<OrderItem>

}
