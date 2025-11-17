
package com.firstapp.mobprogfinalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vouchers")
data class Voucher(
    @PrimaryKey(autoGenerate = true)
    val voucherId: Int = 0,
    val code: String,
    val description: String,
    val discountPercentage: Double, // e.g., 0.10 for 10% off
    val expiryDate: Long, // Store as Long (timestamp)
    val isActive: Boolean = true
)
