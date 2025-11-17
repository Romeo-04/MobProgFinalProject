package com.firstapp.mobprogfinalproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.firstapp.mobprogfinalproject.data.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.security.MessageDigest

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tvProfileName)
        tvEmail = view.findViewById(R.id.tvProfileEmail)
        tvPhone = view.findViewById(R.id.tvProfilePhone)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnLogout = view.findViewById(R.id.btnLogout)

        loadUserProfile()

        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun loadUserProfile() {
        val userId = UserSession.getUserId(requireContext())
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            activity?.runOnUiThread {
                if (user != null) {
                    tvName.text = user.fullName
                    tvEmail.text = user.email
                    tvPhone.text = user.mobileNumber
                }
            }
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_profile, null)

        val etName = dialogView.findViewById<TextInputEditText>(R.id.etEditName)
        val etPhone = dialogView.findViewById<TextInputEditText>(R.id.etEditPhone)

        // Pre-fill current values
        etName.setText(tvName.text)
        etPhone.setText(tvPhone.text)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newName = etName.text.toString().trim()
                val newPhone = etPhone.text.toString().trim()
                
                if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                    updateProfile(newName, newPhone)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProfile(newName: String, newPhone: String) {
        val userId = UserSession.getUserId(requireContext())
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            db.userDao().updateUser(userId, newName, newPhone)
            UserSession.saveUser(
                requireContext(),
                userId,
                newName,
                UserSession.getUserEmail(requireContext())
            )
            
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                loadUserProfile()
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun logout() {
        UserSession.clearSession(requireContext())
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}
