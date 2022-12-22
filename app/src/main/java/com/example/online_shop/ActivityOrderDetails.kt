package com.example.online_shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityOrderDetails : AppCompatActivity() {
    private lateinit var totalPriceView: TextView
    private lateinit var totalCountView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        totalPriceView = findViewById(R.id.tvODItemmmmmmmPrice)
        totalCountView = findViewById(R.id.idODTotallllCount)

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
        totalPriceView.text = "Total Products Price: " + totalProductsPrice(productList)
        totalCountView.text = "Total Products Count: " + totalProductsCount(productList)

        recyclerView.adapter = AdapterOrderDetails(productList, parentOrderId, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun totalProductsCount(productList : ArrayList<Product>) : String {
        var ans = 0
        for (item in productList) {
            ans += item.count_in_bucket!!
        }
        return ans.toString()
    }

    private fun totalProductsPrice(productList : ArrayList<Product>) : String {
        var ans = 0
        for (item in productList) {
            ans += (item.count_in_bucket!! * item.price!!)
        }
        return ans.toString()
    }
}