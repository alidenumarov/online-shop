package com.example.online_shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityOrderDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val intentProducts = intent.getBundleExtra("intentMyOrders")
        var productList = arrayListOf<Product>()
        var parentOrderId = ""
        if (intentProducts != null) {
            val pl = intentProducts.getSerializable("myOrders") as ArrayList<Product>
            productList = pl

            val catId = intentProducts.getSerializable("intentParentMyOrders").toString()
            parentOrderId = catId
        }

        val recyclerView: RecyclerView = findViewById(R.id.idOrderDetails)
        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = AdapterOrderDetails(productList, parentOrderId, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}