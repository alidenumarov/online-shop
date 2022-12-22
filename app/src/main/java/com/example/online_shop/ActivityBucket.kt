package com.example.online_shop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_bucket.*


class ActivityBucket : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var totalCountView: TextView
    private lateinit var totalSumView: TextView
    private lateinit var dbRef : DatabaseReference
    private lateinit var recBucketProductView : RecyclerView
    private lateinit var bucketProductList : ArrayList<Product>
    private lateinit var myOrdersList : ArrayList<MyOrder>
    val userEmail = Firebase.auth.currentUser?.email.toString().replace(".", " ")

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
                R.id.nav_my_orders -> {
                    Toast.makeText(this, "from My Orders", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityMyOrders::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
        bucketProductList = arrayListOf()
        myOrdersList = arrayListOf()
        getUserData(this)
        getMyOrders()

        recBucketProductView = findViewById(R.id.idRVBucketItems)
        val llm = LinearLayoutManager(this)
        recBucketProductView.layoutManager = llm
        recBucketProductView.adapter = AdapterBucket(bucketProductList, this)

        idContinueBtn.setOnClickListener {

            Toast.makeText(this, "Enter Card Data", Toast.LENGTH_LONG).show()

            var bottomFragment = BottomFragment(totalSumView.text.toString(), bucketProductList, myOrdersList, this)
            bottomFragment.show(supportFragmentManager, "TAG")
        }

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
                    val t: GenericTypeIndicator<Map<String, Map<String, Product>>> =
                        object : GenericTypeIndicator<Map<String, Map<String, Product>>>() {}

                    var totalPrice = 0
                    var totalCount = 0
                    if (snapshot.hasChildren()) {
                        bucketProductList = arrayListOf()
                        var mpPr = mutableMapOf<String, Product>()
                        val products = snapshot.getValue(t)
                        products?.forEach { p ->
                            if (userEmail == p.key) {
                                mpPr = p.value as MutableMap<String, Product>
                            }
                        }
                        // make unique
                        for (item in mpPr) {
                            bucketProductList.add(item.value)
                            totalPrice += item.value.price!! * item.value.count_in_bucket!!
                            totalCount += item.value.count_in_bucket!!
                        }

                        recBucketProductView.adapter = AdapterBucket(bucketProductList, context)
                        totalCountView.text = "$totalCount"
                        totalSumView.text = "$totalPrice ₸"
                    }


                } else {
                    handleArrayFunction(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getMyOrders() {
        dbRef = FirebaseDatabase.getInstance().getReference("my_orders").child(userEmail)

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                myOrdersList = arrayListOf()

                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<ArrayList<MyOrder>> =
                        object : GenericTypeIndicator<ArrayList<MyOrder>>() {}
                    val myOrders = snapshot.getValue(t)
                    if (myOrders != null) {
                        myOrdersList = myOrders
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun handleArrayFunction(context: Context)  {
        dbRef = FirebaseDatabase.getInstance().getReference("bucket_items")
        dbRef.get().addOnSuccessListener {
//            recFavView.adapter = AdapterFavourites(favList)
            totalCountView.text = "No items yet"
            totalSumView.text = "0 ₸"
            recBucketProductView.adapter = AdapterBucket(arrayListOf(), context)
        }
    }
}