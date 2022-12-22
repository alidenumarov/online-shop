package com.example.online_shop

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ActivityMyOrders : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var myOrdersAdapter: AdapterMyOrders
    private lateinit var dbRef : DatabaseReference
    private lateinit var recMyOrdersProductView : RecyclerView
    private lateinit var myOrdersList : ArrayList<MyOrder>
    val userEmail = Firebase.auth.currentUser?.email.toString().replace(".", " ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        bottomNavView = findViewById(R.id.bottom_navigation_my_orders)
        bottomNavView.selectedItemId = R.id.nav_my_orders

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

        recMyOrdersProductView = findViewById(R.id.idRVMyOrders)

        myOrdersList = arrayListOf()
        getMyOrders(this)

        val layoutManager = GridLayoutManager(this, 2)

        recMyOrdersProductView.layoutManager = layoutManager

        // on below line we are initializing our adapter
        myOrdersAdapter = AdapterMyOrders(myOrdersList, this)

        recMyOrdersProductView.adapter = myOrdersAdapter
        myOrdersAdapter.notifyDataSetChanged()
    }

    private fun getMyOrders(context: Context) {


        dbRef = FirebaseDatabase.getInstance().getReference("my_orders").child(userEmail)

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                myOrdersList = arrayListOf()

                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<ArrayList<MyOrder>> =
                        object : GenericTypeIndicator<ArrayList<MyOrder>>() {}

                    val myOrders = snapshot.getValue(t)
                    if (myOrders != null) {
                        myOrdersList= myOrders
                    }
                    println("eeeeeeeeeeeeeeeeeeeeeeeeeee")
                    println(myOrdersList)

                    recMyOrdersProductView.adapter = AdapterMyOrders(myOrdersList, context)
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
            recMyOrdersProductView.adapter = AdapterMyOrders(arrayListOf(), context)
        }
    }
}