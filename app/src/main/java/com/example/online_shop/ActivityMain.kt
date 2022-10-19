package com.example.online_shop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class ActivityMain : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var categoryAdapter: AdapterCategory
    lateinit var categoryArrayList: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are initializing
        // our views with their ids.
        categoryRecyclerView = findViewById(R.id.idRVCategories)

        // on below line we are initializing our list
        categoryArrayList = arrayListOf<Category>()

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

    private fun getUserData(context: Context) {

        dbRef = FirebaseDatabase.getInstance().getReference("categories")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList = arrayListOf<Category>()
                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Category::class.java)
                        categoryArrayList.add(user!!)
                    }

                    categoryRecyclerView.adapter = AdapterCategory(categoryArrayList, context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}