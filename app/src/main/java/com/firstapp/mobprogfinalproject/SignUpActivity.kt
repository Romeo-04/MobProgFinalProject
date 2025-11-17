package com.firstapp.mobprogfinalproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.firstapp.mobprogfinalproject.data.AppDatabase
import com.firstapp.mobprogfinalproject.data.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.security.MessageDigest

class SignUpActivity : AppCompatActivity() {
    
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnSignUp: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLogin = findViewById(R.id.tvLogin)

        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (validateInput(name, email, phone, password, confirmPassword)) {
                performSignUp(name, email, phone, password)
            }
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(
        name: String, 
        email: String, 
        phone: String, 
        password: String, 
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.length < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun performSignUp(name: String, email: String, phone: String, password: String) {
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        lifecycleScope.launch {
            // Check if user already exists
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                runOnUiThread {
                    Toast.makeText(this@SignUpActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            // Create new user
            val hashedPassword = hashPassword(password)
            val newUser = User(
                fullName = name,
                email = email,
                mobileNumber = phone,
                passwordHash = hashedPassword
            )

            userDao.insertUser(newUser)

            runOnUiThread {
                Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(password.toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
