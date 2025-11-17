package com.firstapp.mobprogfinalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.CartManager

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var tvTotal: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var btnCheckout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvCartTotal)
        tvEmpty = view.findViewById(R.id.tvEmptyCart)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CartAdapter(
            onQuantityChange = { menuItemId, quantity ->
                CartManager.updateQuantity(menuItemId, quantity)
                updateCart()
            },
            onRemove = { menuItemId ->
                CartManager.removeItem(menuItemId)
                updateCart()
            }
        )
        recyclerView.adapter = adapter

        btnCheckout.setOnClickListener {
            if (!CartManager.isEmpty()) {
                (activity as? MainActivity)?.showCheckoutFragment()
            }
        }

        updateCart()
    }

    override fun onResume() {
        super.onResume()
        updateCart()
    }

    private fun updateCart() {
        val items = CartManager.getCartItems()
        adapter.submitList(items)
        
        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            btnCheckout.visibility = View.GONE
            tvTotal.text = "₱0.00"
        } else {
            tvEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            btnCheckout.visibility = View.VISIBLE
            tvTotal.text = "₱%.2f".format(CartManager.getTotalPrice())
        }
        
        (activity as? MainActivity)?.updateCartBadge()
    }
}
