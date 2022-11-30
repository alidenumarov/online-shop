package com.example.online_shop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class ActivityBucket : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var totalCountView: TextView
    private lateinit var totalSumView: TextView
    private lateinit var dbRef : DatabaseReference
    private lateinit var recBucketProductView : RecyclerView
    private lateinit var bucketProductList : ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bucket)
        bottomNavView = findViewById(R.id.bottom_navigation_bucket)
        totalCountView = findViewById(R.id.tvItemCount)
        totalSumView = findViewById(R.id.tvTotalSum)

        bottomNavView.selectedItemId = R.id.nav_bucket

        bottomNavView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_category -> {
                    Toast.makeText(this, "from Bucket", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_like -> {
                    Toast.makeText(this, "from Bucket", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityFavourites::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_bucket -> {
                    Toast.makeText(this, "from Bucket", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityBucket::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
        bucketProductList = arrayListOf()
        getUserData(this)

        recBucketProductView = findViewById(R.id.idRVBucketItems)
        val llm = LinearLayoutManager(this)
        recBucketProductView.layoutManager = llm
        recBucketProductView.adapter = AdapterBucket(bucketProductList)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun getUserData(context: Context) {
        dbRef = FirebaseDatabase.getInstance().getReference("bucket_items")

        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<Map<String, Product>> =
                        object : GenericTypeIndicator<Map<String, Product>>() {}

                    var totalPrice = 0
                    var totalCount = 0
                    if (snapshot.hasChildren()) {
                        bucketProductList = arrayListOf()
                        val mp = mutableMapOf<String, Product>()
                        val products = snapshot.getValue(t)
                        products?.forEach { p ->
                            mp[p.value.id.toString()] = p.value
                        }
                        // make unique
                        for (item in mp) {
                            bucketProductList.add(item.value)
                            totalPrice += item.value.price!! * item.value.count_in_bucket!!
                            totalCount += item.value.count_in_bucket!!
                        }

                        recBucketProductView.adapter = AdapterBucket(bucketProductList)
                        totalCountView.text = "$totalCount"
                        totalSumView.text = "$totalPrice ₸"
                    }
                } else {
                    handleArrayFunction()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun handleArrayFunction()  {
        dbRef = FirebaseDatabase.getInstance().getReference("bucket_items")
        dbRef.get().addOnSuccessListener {
//            recFavView.adapter = AdapterFavourites(favList)
            recBucketProductView.adapter = AdapterBucket(arrayListOf())
        }
    }
}