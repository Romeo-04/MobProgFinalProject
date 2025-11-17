package com.firstapp.mobprogfinalproject.data

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int
)

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addItem(menuItem: MenuItem, quantity: Int = 1) {
        val existingItem = cartItems.find { it.menuItem.menuItemId == menuItem.menuItemId }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            cartItems.add(CartItem(menuItem, quantity))
        }
    }

    fun updateQuantity(menuItemId: Int, quantity: Int) {
        val item = cartItems.find { it.menuItem.menuItemId == menuItemId }
        if (item != null) {
            if (quantity <= 0) {
                cartItems.remove(item)
            } else {
                item.quantity = quantity
            }
        }
    }

    fun removeItem(menuItemId: Int) {
        cartItems.removeAll { it.menuItem.menuItemId == menuItemId }
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.menuItem.price * it.quantity }
    }

    fun getItemCount(): Int {
        return cartItems.sumOf { it.quantity }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun isEmpty(): Boolean = cartItems.isEmpty()
}
