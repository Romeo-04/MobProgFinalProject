package com.firstapp.mobprogfinalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.MenuItem

class MenuAdapter(
    private val onAddToCart: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var menuItems = listOf<MenuItem>()

    fun submitList(items: List<MenuItem>) {
        menuItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount() = menuItems.size

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvMenuName)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvMenuDescription)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvMenuPrice)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvMenuCategory)
        private val btnAdd: Button = itemView.findViewById(R.id.btnAddToCart)

        fun bind(item: MenuItem) {
            tvName.text = item.name
            tvDescription.text = item.description
            tvPrice.text = "₱%.2f".format(item.price)
            tvCategory.text = item.category
            btnAdd.setOnClickListener { onAddToCart(item) }
        }
    }
}
