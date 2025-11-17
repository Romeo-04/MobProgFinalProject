
package com.firstapp.mobprogfinalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val menuItemId: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String, // e.g., "Silog", "Snacks", "Drinks"
    val imageUrl: String? = null // Optional image for the item
)
