package com.example.online_shop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*


class ActivityLike : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var dbRef : DatabaseReference
    private lateinit var recFavView : RecyclerView
    private lateinit var favList : ArrayList<Favourite>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)


        bottomNavView = findViewById(R.id.bottom_navigation_like)

        bottomNavView.selectedItemId = R.id.nav_like

        bottomNavView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_category -> {
                    Toast.makeText(this, "from Favourites", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_like -> {
                    Toast.makeText(this, "from Favourites", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityLike::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
        favList = arrayListOf()
        getUserData(this)

        recFavView = findViewById(R.id.idRVFavourites)
        val llm = LinearLayoutManager(this)
        recFavView.layoutManager = llm
        recFavView.adapter = AdapterFavourites(favList)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun getUserData(context: Context) {
        dbRef = FirebaseDatabase.getInstance().getReference("likes")

        dbRef.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<Map<String, Favourite>> =
                        object : GenericTypeIndicator<Map<String, Favourite>>() {}

                    if (snapshot.hasChildren()) {
                        favList = arrayListOf()
                        val mp = mutableMapOf<String, Favourite>()
                        val products = snapshot.getValue(t)
                        products?.forEach { p ->
                            mp[p.value.id.toString()] = p.value
                        }
                        // make unique
                        for (item in mp) {
                            favList.add(item.value)
                        }
                        println(favList)

                        recFavView.adapter = AdapterFavourites(favList)
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
        dbRef = FirebaseDatabase.getInstance().getReference("likes")
        dbRef.get().addOnSuccessListener {
//            recFavView.adapter = AdapterFavourites(favList)
            recFavView.adapter = AdapterFavourites(arrayListOf())
        }
    }
}