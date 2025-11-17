
package com.firstapp.mobprogfinalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.firstapp.mobprogfinalproject.data.AppDatabase
import com.firstapp.mobprogfinalproject.data.CartManager
import com.firstapp.mobprogfinalproject.data.MenuItem
import com.firstapp.mobprogfinalproject.data.Voucher
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var topAppBar: MaterialToolbar
    private var cartBadge: BadgeDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if user is logged in
        if (!UserSession.isLoggedIn(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        topAppBar = findViewById(R.id.topAppBar)
        bottomNav = findViewById(R.id.bottomNav)

        setupBottomNavigation()
        seedDatabase()

        // Show menu fragment by default
        if (savedInstanceState == null) {
            showFragment(MenuFragment())
            bottomNav.selectedItemId = R.id.nav_menu
        }
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu -> {
                    showFragment(MenuFragment())
                    topAppBar.title = "Menu"
                    true
                }
                R.id.nav_cart -> {
                    showFragment(CartFragment())
                    topAppBar.title = "Cart"
                    true
                }
                R.id.nav_orders -> {
                    showFragment(OrdersFragment())
                    topAppBar.title = "Orders"
                    true
                }
                R.id.nav_vouchers -> {
                    showFragment(VouchersFragment())
                    topAppBar.title = "Vouchers"
                    true
                }
                R.id.nav_profile -> {
                    showFragment(ProfileFragment())
                    topAppBar.title = "Profile"
                    true
                }
                else -> false
            }
        }

        // Setup cart badge
        cartBadge = bottomNav.getOrCreateBadge(R.id.nav_cart)
        cartBadge?.backgroundColor = getColor(android.R.color.black)
        cartBadge?.badgeTextColor = getColor(android.R.color.white)
        updateCartBadge()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun showCheckoutFragment() {
        showFragment(CheckoutFragment())
        topAppBar.title = "Checkout"
        bottomNav.menu.findItem(R.id.nav_cart)?.isChecked = false
    }

    fun showOrdersFragment() {
        showFragment(OrdersFragment())
        topAppBar.title = "Orders"
        bottomNav.selectedItemId = R.id.nav_orders
    }

    fun updateCartBadge() {
        val count = CartManager.getItemCount()
        if (count > 0) {
            cartBadge?.isVisible = true
            cartBadge?.number = count
        } else {
            cartBadge?.isVisible = false
        }
    }

    private fun seedDatabase() {
        val db = AppDatabase.getDatabase(this)
        
        lifecycleScope.launch {
            // Seed menu items
            val existingItems = db.menuItemDao().getMenuItemCount()
            if (existingItems == 0) {
                val sampleMenuItems = listOf(
                    MenuItem(name = "Porksilog", description = "Fried pork chop, egg, and garlic rice", price = 75.0, category = "Silog"),
                    MenuItem(name = "Tapsilog", description = "Beef tapa, egg, and garlic rice", price = 80.0, category = "Silog"),
                    MenuItem(name = "Chicksilog", description = "Fried chicken, egg, and garlic rice", price = 70.0, category = "Silog"),
                    MenuItem(name = "Hotsilog", description = "Hotdog, egg, and garlic rice", price = 60.0, category = "Silog"),
                    MenuItem(name = "Burger", description = "Classic beef burger", price = 50.0, category = "Snacks"),
                    MenuItem(name = "Hotdog Sandwich", description = "Classic hotdog sandwich", price = 40.0, category = "Snacks"),
                    MenuItem(name = "Fries", description = "Crispy french fries", price = 35.0, category = "Snacks"),
                    MenuItem(name = "Iced Coffee", description = "Cold brewed coffee", price = 45.0, category = "Drinks"),
                    MenuItem(name = "Gulaman", description = "Sweet jelly drink", price = 25.0, category = "Drinks"),
                    MenuItem(name = "Bottled Water", description = "500ml bottled water", price = 20.0, category = "Drinks")
                )
                db.menuItemDao().insertAll(sampleMenuItems)
            }

            // Seed vouchers
            val existingVouchers = db.voucherDao().getVoucherCount()
            if (existingVouchers == 0) {
                val sampleVouchers = listOf(
                    Voucher(
                        code = "WELCOME10",
                        description = "Welcome discount for new customers",
                        discountPercentage = 0.10,
                        expiryDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000), // 30 days
                        isActive = true
                    ),
                    Voucher(
                        code = "SILOG15",
                        description = "15% off on all Silog meals",
                        discountPercentage = 0.15,
                        expiryDate = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000), // 60 days
                        isActive = true
                    ),
                    Voucher(
                        code = "PANDOG20",
                        description = "20% off on orders above ₱200",
                        discountPercentage = 0.20,
                        expiryDate = System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000), // 90 days
                        isActive = true
                    )
                )
                db.voucherDao().insertAll(sampleVouchers)
            }
        }
    }
}
