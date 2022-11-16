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

class ActivityRegister : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLoginTextView : TextView = findViewById(R.id.textView_go_to_login_page)
        auth = Firebase.auth

        // при нажатии на "Go to Login Page", открываем страницу логина
        goToLoginTextView.setOnClickListener {
            var goToRegisterIntent = Intent(this, ActivityLogin::class.java)
            startActivity(goToRegisterIntent)
        }

        val registerButton : Button = findViewById(R.id.button_register)
        registerButton.setOnClickListener {
            performSignUp()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun performSignUp() {
        val email = findViewById<EditText>(R.id.editText_email_register).text.toString()
        val password = findViewById<EditText>(R.id.editText_password_register).text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)

                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}