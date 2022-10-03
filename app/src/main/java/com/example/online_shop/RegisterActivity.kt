package com.example.online_shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLoginTextView : TextView = findViewById(R.id.textView_go_to_login_page)

        // при нажатии на "Go to Login Page", открываем страницу логина
        goToLoginTextView.setOnClickListener {
            var goToRegisterIntent = Intent(this, LoginActivity::class.java)
            startActivity(goToRegisterIntent)
        }
    }
}