package com.example.online_shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityProduct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val args = intent.getBundleExtra("bundle")
        var productList = arrayListOf<Product>()
        if (args != null) {
            productList = args.getSerializable("products") as ArrayList<Product>
        }

        val recyclerView: RecyclerView = findViewById(R.id.idProductList)
        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = AdapterProduct(getUserData(productList))


        println(productList)
    }

    private fun getUserData(productList: ArrayList<Product>): ArrayList<Product> {
        val products = arrayListOf<Product>()

        for (pr in productList) {
            products.add(pr)
        }

        return products
    }
}