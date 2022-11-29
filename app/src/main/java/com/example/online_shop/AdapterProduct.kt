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
        val toBucketBtn : Button = itemView.findViewById(R.id.btnToBucketFromProduct)
        val likeImg : ImageView = itemView.findViewById(R.id.imgLikeProduct)
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
            holder.likeImg.setImageResource(R.drawable.ic_liked)
        } else {
            holder.likeImg.setImageResource(R.drawable.ic_unliked)
        }

        if (products[position].in_bucket == 1) {
            holder.toBucketBtn.text = "remove from bucket".uppercase()
        } else {
            holder.toBucketBtn.text = "to bucket".uppercase()
        }

        holder.toBucketBtn.setOnClickListener {
            val product = products[position]
            holder.db = FirebaseDatabase.getInstance()
            val res = addOrRemoveInBucket(product, holder.db)
            if (res) {
                val btnTxt = holder.toBucketBtn.text
                if (btnTxt.toString().uppercase() == "to bucket".uppercase()) {
                    holder.toBucketBtn.text = "remove from bucket".uppercase()
                } else {
                    holder.toBucketBtn.text = "to bucket".uppercase()
                }
            }
        }

        holder.likeImg.setOnClickListener{
            holder.db = FirebaseDatabase.getInstance()
            val product = products[position]
            val res = addOrRemoveInFavs(product, holder.db)
            if (res) {
                if (holder.likeImg.id == R.drawable.ic_liked) {
                    holder.likeImg.setImageResource(R.drawable.ic_unliked)
                } else {
                    holder.likeImg.setImageResource(R.drawable.ic_liked)
                }
            }
        }
    }

    override fun getItemCount() = products.size

    private fun addOrRemoveInFavs(product : Product, dbRef : FirebaseDatabase): Boolean {
        var result = true
        if (product.in_favs == 0) {
            val dbLike = dbRef.getReference("likes")
            product.in_favs = 1
            product.parent_cat_id = parentCatId
            dbLike.child(product.id.toString()).setValue(product)

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(parentCatId).child("products").
            child(product.id.toString()).child("in_favs").setValue(1).
            addOnSuccessListener {
                println("Product was added to favs: $product")
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
                println("Product was removed from favs: $product")
            }
            .addOnFailureListener {
                result = false
            }

        }

        return result
    }

    private fun addOrRemoveInBucket(product : Product, dbRef : FirebaseDatabase): Boolean {
        var result = true
        if (product.in_bucket == 0) {
            val dbBucket = dbRef.getReference("bucket_items")
            product.in_bucket = 1
            product.parent_cat_id = parentCatId
            dbBucket.child(product.id.toString()).setValue(product)

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(parentCatId).child("products").
            child(product.id.toString()).child("in_bucket").setValue(1).
            addOnSuccessListener {
                println("product was added to bucket: $product")
            }
                .addOnFailureListener {
                    result = false
                }

            val dbLike = dbRef.getReference("likes")
            val a = dbLike.child(product.id.toString()).get()
            a.addOnSuccessListener { it ->
                if (it.value != null) {
                    dbLike.child(product.id.toString()).child("in_bucket").setValue(1)
                }
            }


        } else {
            val dbLike = dbRef.getReference("bucket_items")
            dbLike.child(product.id.toString()).removeValue()

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(parentCatId).child("products").
            child(product.id.toString()).child("in_bucket").setValue(0).
            addOnSuccessListener {
                println("product was removed from bucket: $product")
            }
                .addOnFailureListener {
                    result = false
                }

        }

        return result
    }

}