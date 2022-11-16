package com.example.online_shop

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class AdapterProduct(private val products: ArrayList<Product>, var parentCatId: String, val itemLayout: Int) : RecyclerView
.Adapter<AdapterProduct.ProductViewHolder>() {


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val productIV: ImageView = itemView.findViewById(R.id.idProductImgUrl)
        val likeBtn : Button = itemView.findViewById(R.id.btnLike)
        lateinit var db : FirebaseDatabase

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_layout, parent, false)
        return ProductViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.productName.text = products[position].name
        holder.productPrice.text = products[position].price.toString() + " ₸"
        var imageUrl = products[position].image_url
        if (imageUrl == "") {
            imageUrl = "https://resources.cdn-kaspi.kz/shop/medias/sys_master/images/images/h65/h0f/33125684084766/apple-macbook-air-2020-13-3-mgn63-seryj-100797845-1-Container.jpg"
        }
        Picasso.get().load(imageUrl).into(holder.productIV)

        if (products[position].in_favs == 1) {
            holder.likeBtn.text = "Dislike"
        } else {
            holder.likeBtn.text = "Like"
        }

        holder.likeBtn.setOnClickListener {
            holder.db = FirebaseDatabase.getInstance()
            val product = products[position]
            val res = addToBucket(product, holder.db)
            if (res) {
                holder.likeBtn.text = "Dislike"
            } else {
                holder.likeBtn.text = "Like"
            }
        }
    }

    override fun getItemCount() = products.size

    private fun addToBucket(product : Product, dbRef : FirebaseDatabase): Boolean {
        var result = true
        if (product.in_favs == 0) {
            val dbLike = dbRef.getReference("likes")
            product.in_favs = 1
            product.parent_cat_id = parentCatId
            println("imggg22222")
            println(product.image_url)
            dbLike.child(product.id.toString()).setValue(product)

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(parentCatId).child("products").
            child(product.id.toString()).child("in_favs").setValue(1).
            addOnSuccessListener {
                println("Product was added to bucket: $product")
            }
                .addOnFailureListener {
                    result = false
                }
        } else {
            val dbLike = dbRef.getReference("likes")
            dbLike.child(product.id.toString()).removeValue()

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(parentCatId).child("products").
            child(product.id.toString()).child("in_favs").setValue(0).
            addOnSuccessListener {
                println("Product was removed from bucket: $product")
            }
                .addOnFailureListener {
                    result = false
                }

        }

        return result
    }
}