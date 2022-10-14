package com.example.chat_d

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var etMail : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        etMail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignup)
        btnLogin = findViewById(R.id.btnLogin)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            val email = etMail.text.toString()
            val pass = etPassword.text.toString()

            login(email, pass)
        }
    }

    private fun login(email: String, pass: String) {
        try {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "login successful", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@Login, "Invalid Credentials", Toast.LENGTH_LONG).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(this@Login, "Enter details", Toast.LENGTH_LONG).show()
        }
    }

}