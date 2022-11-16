package com.example.online_shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*


class ActivityMain : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var categoryAdapter: AdapterCategory
    lateinit var categoryArrayList: ArrayList<Category>
    lateinit var categoryMap: MutableMap<String, Category>
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView = findViewById(R.id.bottom_navigation)
        bottomNavView.selectedItemId = R.id.nav_category
        bottomNavView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_category -> {
                    Toast.makeText(this, "from Categories", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_like -> {
                    Toast.makeText(this, "from Categories", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityLike::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }

        // on below line we are initializing
        // our views with their ids.
        categoryRecyclerView = findViewById(R.id.idRVCategories)

        // on below line we are initializing our list
        categoryArrayList = arrayListOf()
        categoryMap = mutableMapOf()

        // on below line we are creating a variable
        // for our grid layout manager and specifying
        // column count as 2
        val layoutManager = GridLayoutManager(this, 2)

        categoryRecyclerView.layoutManager = layoutManager

        // on below line we are initializing our adapter
        categoryAdapter = AdapterCategory(categoryArrayList, this)

        // on below line we are setting
        // adapter to our recycler view.
        categoryRecyclerView.adapter = categoryAdapter

        categoryAdapter.notifyDataSetChanged()

        // on below line we are adding data to our list
        getUserData(this)

    }

    inline fun <reified T: Any> Any.cast(): T{
        return this as T
    }

    private fun getUserData(context: Context) {

        dbRef = FirebaseDatabase.getInstance().getReference("categories")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList = arrayListOf()
                categoryMap = mutableMapOf()

                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<Map<String, Category>> =
                        object : GenericTypeIndicator<Map<String, Category>>() {}
                    val categories = snapshot.getValue(t)
                    categories?.forEach { p ->
                        categoryArrayList.add(p.value)
                    }

                    categoryRecyclerView.adapter = AdapterCategory(categoryArrayList, context)
                }

//                if (snapshot.exists()) {
//
//                    val product = Product(id= "101", name = "asdfasdf", price = 123, image_url = "https://www.mechta.kz/images/product/51492/26260002255_1.jpg")
//
//                    val mpProduct = mutableMapOf<String, Product>()
//                    mpProduct["2345"]= product
//
//                    categoryMap["1"]= Category(id= "1", name="11234", image_url = "https://static.frame.work/aqqzr02l9hrlc1hgbnjxkqx7m1ik",
//                    products = mpProduct)
//
//                    println("cccccccccccc")
//                    println(categoryMap)
//                    for (i in categoryMap.iterator()) {
//                        categoryArrayList.add(i.value)
//                    }
//                    categoryRecyclerView.adapter = AdapterCategory(categoryArrayList, context)
//                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}