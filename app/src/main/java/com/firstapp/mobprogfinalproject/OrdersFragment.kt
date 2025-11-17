package com.firstapp.mobprogfinalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.AppDatabase
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvOrders)
        tvEmpty = view.findViewById(R.id.tvEmptyOrders)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = OrdersAdapter()
        recyclerView.adapter = adapter

        loadOrders()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        val userId = UserSession.getUserId(requireContext())
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            db.orderDao().getOrdersForUser(userId).collect { orders ->
                activity?.runOnUiThread {
                    if (orders.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmpty.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter.submitList(orders)
                    }
                }
            }
        }
    }
}
