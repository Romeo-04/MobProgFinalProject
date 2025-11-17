
package com.firstapp.mobprogfinalproject

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.firstapp.mobprogfinalproject.data.AppDatabase
import com.firstapp.mobprogfinalproject.data.MenuItem
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Database Test Code ---
        val db = AppDatabase.getDatabase(this)
        val menuItemDao = db.menuItemDao()

        lifecycleScope.launch {
            // 1. Create sample menu items
            val sampleMenuItems = listOf(
                MenuItem(name = "Porksilog", description = "Fried pork chop, egg, and garlic rice", price = 75.0, category = "Silog"),
                MenuItem(name = "Tapsilog", description = "Beef tapa, egg, and garlic rice", price = 80.0, category = "Silog"),
                MenuItem(name = "Hotdog", description = "Classic hotdog sandwich", price = 40.0, category = "Snacks"),
                MenuItem(name = "Gulaman", description = "Sweet jelly drink", price = 25.0, category = "Drinks")
            )

            // 2. Insert them into the database
            menuItemDao.insertAll(sampleMenuItems)
            Log.d("DB_TEST", "Inserted sample menu items.")

            // 3. Read them back from the database
            menuItemDao.getAllMenuItems().collect { menuItems ->
                if (menuItems.isNotEmpty()) {
                    Log.d("DB_TEST", "--- All Menu Items ---")
                    menuItems.forEach { item ->
                        Log.d("DB_TEST", "Item: ${item.name}, Price: ₱${item.price}")
                    }
                } else {
                    Log.d("DB_TEST", "No menu items found in the database.")
                }
            }
        }
    }
}
