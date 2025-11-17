package com.firstapp.mobprogfinalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.CartItem

class CartAdapter(
    private val onQuantityChange: (Int, Int) -> Unit,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems = listOf<CartItem>()

    fun submitList(items: List<CartItem>) {
        cartItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount() = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvCartItemName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        private val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        private val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        private val btnRemove: Button = itemView.findViewById(R.id.btnRemove)

        fun bind(item: CartItem) {
            tvName.text = item.menuItem.name
            tvPrice.text = "₱%.2f".format(item.menuItem.price)
            tvQuantity.text = item.quantity.toString()
            tvSubtotal.text = "₱%.2f".format(item.menuItem.price * item.quantity)

            btnIncrease.setOnClickListener {
                onQuantityChange(item.menuItem.menuItemId, item.quantity + 1)
            }

            btnDecrease.setOnClickListener {
                if (item.quantity > 1) {
                    onQuantityChange(item.menuItem.menuItemId, item.quantity - 1)
                }
            }

            btnRemove.setOnClickListener {
                onRemove(item.menuItem.menuItemId)
            }
        }
    }
}
