package com.example.online_shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivityLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerText : TextView = findViewById(R.id.textView_go_to_register)
        auth = Firebase.auth

        // при нажатии на "Go to Register", открываем страницу регистрации
        registerText.setOnClickListener{
            val registerIntent = Intent(this, ActivityRegister::class.java)
            startActivity(registerIntent)
        }

        val loginButton : Button = findViewById(R.id.button_login)
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.editText_email_login).text.toString()
        val password = findViewById<EditText>(R.id.editText_password_login).text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)

                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}