package com.firstapp.mobprogfinalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.Order
import java.text.SimpleDateFormat
import java.util.Locale

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private var orders = listOf<Order>()

    fun submitList(items: List<Order>) {
        orders = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        private val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        private val tvOrderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        private val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        private val tvPaymentMode: TextView = itemView.findViewById(R.id.tvPaymentMode)

        fun bind(order: Order) {
            tvOrderId.text = "#${order.orderId}"
            
            val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
            tvOrderDate.text = dateFormat.format(order.orderDate)
            
            tvOrderTotal.text = "₱%.2f".format(order.totalPrice)
            tvOrderStatus.text = order.status
            tvPaymentMode.text = order.paymentMode
        }
    }
}
