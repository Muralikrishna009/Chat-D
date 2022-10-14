package com.example.chat_d

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etMail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        etName = findViewById(R.id.etName)
        etMail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignup)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val name = etName.text.toString()
            val mail = etMail.text.toString()
            val pass = etPassword.text.toString()

            signup(name, mail, pass)
        }
    }

    private fun signup(name: String, mail: String, pass: String) {
        try {
            auth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        addUserToDatabase(name, mail, auth.currentUser?.uid!!)
                        val intent = Intent(this@SignUp, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@SignUp,
                            "Error: Can not create account",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("ErrorsCheck", it.result.toString())
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(this@SignUp, "Enter Details", Toast.LENGTH_LONG).show()
        }
    }

    private fun addUserToDatabase(name: String, mail: String, uid: String) {
        dbRef = FirebaseDatabase.getInstance().reference

        dbRef.child("Users").child(uid).setValue(User(name, mail, uid))
    }
}