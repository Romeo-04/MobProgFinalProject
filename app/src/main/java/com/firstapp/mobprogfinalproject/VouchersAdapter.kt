package com.firstapp.mobprogfinalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.Voucher
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VouchersAdapter : RecyclerView.Adapter<VouchersAdapter.VoucherViewHolder>() {

    private var vouchers = listOf<Voucher>()

    fun submitList(items: List<Voucher>) {
        vouchers = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_voucher, parent, false)
        return VoucherViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(vouchers[position])
    }

    override fun getItemCount() = vouchers.size

    inner class VoucherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCode: TextView = itemView.findViewById(R.id.tvVoucherCode)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvVoucherDescription)
        private val tvDiscount: TextView = itemView.findViewById(R.id.tvVoucherDiscount)
        private val tvExpiry: TextView = itemView.findViewById(R.id.tvVoucherExpiry)

        fun bind(voucher: Voucher) {
            tvCode.text = voucher.code
            tvDescription.text = voucher.description
            tvDiscount.text = "${(voucher.discountPercentage * 100).toInt()}% OFF"
            
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val expiryDate = Date(voucher.expiryDate)
            tvExpiry.text = "Valid until ${dateFormat.format(expiryDate)}"
        }
    }
}
