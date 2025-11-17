package com.firstapp.mobprogfinalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.mobprogfinalproject.data.AppDatabase
import com.firstapp.mobprogfinalproject.data.CartManager
import com.firstapp.mobprogfinalproject.data.MenuItem
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvMenu)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MenuAdapter { menuItem ->
            addToCart(menuItem)
        }
        recyclerView.adapter = adapter

        loadMenuItems()
    }

    private fun loadMenuItems() {
        val db = AppDatabase.getDatabase(requireContext())
        val menuItemDao = db.menuItemDao()

        lifecycleScope.launch {
            menuItemDao.getAllMenuItems().collect { items ->
                adapter.submitList(items)
            }
        }
    }

    private fun addToCart(menuItem: MenuItem) {
        CartManager.addItem(menuItem, 1)
        Toast.makeText(requireContext(), "${menuItem.name} added to cart", Toast.LENGTH_SHORT).show()
        
        // Update badge count if needed
        (activity as? MainActivity)?.updateCartBadge()
    }
}
