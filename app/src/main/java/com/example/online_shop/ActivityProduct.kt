package com.example.online_shop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ActivityProduct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val intentProducts = intent.getBundleExtra("intentProducts")
        val productList = arrayListOf<Product>()
        var favList = arrayListOf<Product>()
        var inBucketList = arrayListOf<Product>()
        var parentCatId = ""
        if (intentProducts != null) {
            val productMap = intentProducts.getSerializable("products") as Map<String, Product>
            for (i in productMap) {
                productList.add(i.value)
            }
            if (intentProducts.getSerializable("favsList") != null) {
                favList = intentProducts.getSerializable("favsList") as ArrayList<Product>
            }
            if (intentProducts.getSerializable("inBucketList") != null) {
                inBucketList = intentProducts.getSerializable("inBucketList") as ArrayList<Product>
            }
            val catId = intentProducts.getSerializable("intentParentCategory").toString()
            parentCatId = catId
        }

        val recyclerView: RecyclerView = findViewById(R.id.idProductList)
        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = AdapterProduct(productList, favList, inBucketList, parentCatId, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}