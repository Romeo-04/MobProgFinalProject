
package com.firstapp.mobprogfinalproject.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val orderId: Int = 0,
    val customerId: Int,
    val orderDate: Date,
    val totalPrice: Double,
    val status: String, // e.g., "Pending", "In Progress", "Ready for Pickup", "Completed"
    val paymentMode: String // e.g., "GCash", "Cash"
)
