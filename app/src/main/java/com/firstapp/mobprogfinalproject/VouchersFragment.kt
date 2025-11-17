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

class VouchersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VouchersAdapter
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vouchers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvVouchers)
        tvEmpty = view.findViewById(R.id.tvEmptyVouchers)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = VouchersAdapter()
        recyclerView.adapter = adapter

        loadVouchers()
    }

    private fun loadVouchers() {
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            db.voucherDao().getActiveVouchers().collect { vouchers ->
                activity?.runOnUiThread {
                    if (vouchers.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmpty.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter.submitList(vouchers)
                    }
                }
            }
        }
    }
}
