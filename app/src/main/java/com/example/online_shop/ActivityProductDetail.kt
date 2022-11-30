package com.example.online_shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ActivityProductDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intentProducts = intent.getBundleExtra("intentProductDetails")
        var productDetail = Product()
        if (intentProducts != null) {
            val pd = intentProducts.getSerializable("productDetails") as Product
            productDetail = pd
        }

        val imageUrl = productDetail.image_url
        val imgView : ImageView = findViewById(R.id.idProductDetailIV)
        Picasso.get().load(imageUrl).into(imgView)

        val textView : TextView = findViewById(R.id.idProductNameTV)
        textView.text = productDetail.name

        val recyclerView: RecyclerView = findViewById(R.id.idRVComments)
        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm

        var listt = arrayListOf<Comment>()
        listt.add(Comment(id = "11", "eqasdfasdfas fa asdfas f asd f", "Aliden"))
//        recyclerView.adapter = AdapterComment(getUserData(productDetail.comments!!), this)
        recyclerView.adapter = AdapterComment(listt, this)
    }

    private fun getUserData(commentMap: Map<String, Comment>): ArrayList<Comment> {
        println("map")
        println(commentMap)
        val commentList = arrayListOf<Comment>()

        for (c in commentMap) {
            commentList.add(c.value)
        }
        println("listttt")
        println(commentList)

        return commentList
    }
}