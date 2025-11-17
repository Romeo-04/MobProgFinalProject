package com.firstapp.mobprogfinalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.firstapp.mobprogfinalproject.data.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if already logged in
        if (UserSession.isLoggedIn(this)) {
            navigateToMain()
            return
        }
        
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignUp = findViewById(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(email, password)
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        lifecycleScope.launch {
            val user = userDao.getUserByEmail(email)
            
            if (user == null) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val hashedPassword = hashPassword(password)
            if (user.passwordHash == hashedPassword) {
                UserSession.saveUser(this@LoginActivity, user.userId, user.fullName, user.email)
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Welcome back, ${user.fullName}!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(password.toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
