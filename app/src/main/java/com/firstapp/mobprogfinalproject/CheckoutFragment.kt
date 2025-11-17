package com.firstapp.mobprogfinalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.firstapp.mobprogfinalproject.data.*
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.util.Date

class CheckoutFragment : Fragment() {

    private lateinit var tvSubtotal: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var rgDeliveryType: RadioGroup
    private lateinit var etAddress: TextInputEditText
    private lateinit var llAddressContainer: LinearLayout
    private lateinit var rgPaymentMethod: RadioGroup
    private lateinit var etVoucherCode: TextInputEditText
    private lateinit var btnApplyVoucher: Button
    private lateinit var btnPlaceOrder: Button

    private var appliedVoucher: Voucher? = null
    private var discountAmount = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSubtotal = view.findViewById(R.id.tvSubtotal)
        tvDiscount = view.findViewById(R.id.tvDiscount)
        tvTotal = view.findViewById(R.id.tvTotal)
        rgDeliveryType = view.findViewById(R.id.rgDeliveryType)
        etAddress = view.findViewById(R.id.etAddress)
        llAddressContainer = view.findViewById(R.id.llAddressContainer)
        rgPaymentMethod = view.findViewById(R.id.rgPaymentMethod)
        etVoucherCode = view.findViewById(R.id.etVoucherCode)
        btnApplyVoucher = view.findViewById(R.id.btnApplyVoucher)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)

        setupListeners()
        updatePrices()
    }

    private fun setupListeners() {
        rgDeliveryType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbDelivery -> llAddressContainer.visibility = View.VISIBLE
                R.id.rbPickup -> llAddressContainer.visibility = View.GONE
            }
        }

        btnApplyVoucher.setOnClickListener {
            applyVoucher()
        }

        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun applyVoucher() {
        val code = etVoucherCode.text.toString().trim()
        if (code.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a voucher code", Toast.LENGTH_SHORT).show()
            return
        }

        val db = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            db.voucherDao().getActiveVouchers().collect { vouchers ->
                val voucher = vouchers.find { it.code.equals(code, ignoreCase = true) }
                if (voucher != null) {
                    // Check if expired
                    if (voucher.expiryDate < System.currentTimeMillis()) {
                        Toast.makeText(requireContext(), "Voucher has expired", Toast.LENGTH_SHORT).show()
                        return@collect
                    }
                    
                    appliedVoucher = voucher
                    Toast.makeText(requireContext(), "Voucher applied!", Toast.LENGTH_SHORT).show()
                    updatePrices()
                } else {
                    Toast.makeText(requireContext(), "Invalid voucher code", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updatePrices() {
        val subtotal = CartManager.getTotalPrice()
        tvSubtotal.text = "₱%.2f".format(subtotal)

        discountAmount = if (appliedVoucher != null) {
            subtotal * appliedVoucher!!.discountPercentage
        } else {
            0.0
        }

        tvDiscount.text = "-₱%.2f".format(discountAmount)
        val total = subtotal - discountAmount
        tvTotal.text = "₱%.2f".format(total)
    }

    private fun placeOrder() {
        // Validate delivery type
        val deliveryType = when (rgDeliveryType.checkedRadioButtonId) {
            R.id.rbDelivery -> {
                val address = etAddress.text.toString().trim()
                if (address.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter delivery address", Toast.LENGTH_SHORT).show()
                    return
                }
                "Delivery"
            }
            R.id.rbPickup -> "Store Pickup"
            else -> {
                Toast.makeText(requireContext(), "Please select delivery type", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Validate payment method
        val paymentMode = when (rgPaymentMethod.checkedRadioButtonId) {
            R.id.rbCash -> "Cash on $deliveryType"
            R.id.rbCashless -> "Cashless"
            else -> {
                Toast.makeText(requireContext(), "Please select payment method", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val userId = UserSession.getUserId(requireContext())
        val totalPrice = CartManager.getTotalPrice() - discountAmount

        val db = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            try {
                // Create order
                val order = Order(
                    customerId = userId,
                    orderDate = Date(),
                    totalPrice = totalPrice,
                    status = "Pending",
                    paymentMode = paymentMode
                )
                val orderId = db.orderDao().insertOrder(order)

                // Create order items
                val orderItems = CartManager.getCartItems().map {
                    OrderItem(
                        orderId = orderId.toInt(),
                        menuItemId = it.menuItem.menuItemId,
                        quantity = it.quantity
                    )
                }
                db.orderDao().insertOrderItems(orderItems)

                // Clear cart
                CartManager.clearCart()

                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.showOrdersFragment()
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Error placing order: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
