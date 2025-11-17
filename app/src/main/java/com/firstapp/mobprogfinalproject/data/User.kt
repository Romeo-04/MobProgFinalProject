
package com.firstapp.mobprogfinalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val fullName: String,
    val email: String,
    val mobileNumber: String,
    val passwordHash: String // Storing a hash, not the plain password
)
