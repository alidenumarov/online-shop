package com.example.online_shop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.io.Serializable

class AdapterProduct(private val products: ArrayList<Product>,
                     private var favList: ArrayList<Product>,
                     private var inBucketList: ArrayList<Product>,
                     var parentCatId: String,
                     private val ctx: Context,
) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val productIV: ImageView = itemView.findViewById(R.id.idProductImgUrl)
        val toBucketBtn : Button = itemView.findViewById(R.id.btnToBucketFromProduct)
        val likeImg : ImageView = itemView.findViewById(R.id.imgLikeProduct)
        lateinit var db : FirebaseDatabase
        val userEmail = Firebase.auth.currentUser?.email.toString().replace(".", " ")
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

        var inFavs = false
        for (item in favList) {
            if (item.id == products[position].id) {
                inFavs = true
                holder.likeImg.setImageResource(R.drawable.ic_liked)
                break
            }
        }
        if (!inFavs) {
            holder.likeImg.setImageResource(R.drawable.ic_unliked)
        }

        var inBuket = false
        for (item in inBucketList) {
            if (item.id == products[position].id) {
                inBuket = true
                holder.toBucketBtn.text = "remove from bucket".uppercase()
                break
            }
        }
        if (!inBuket) {
            holder.toBucketBtn.text = "to bucket".uppercase()
        }

        holder.toBucketBtn.setOnClickListener {
            val product = products[position]
            holder.db = FirebaseDatabase.getInstance()
            val res = addOrRemoveInBucket(product, holder)
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
            val res = addOrRemoveInFavs(product, holder)
            if (res) {
                holder.likeImg.setImageResource(R.drawable.ic_liked)
            } else {
                holder.likeImg.setImageResource(R.drawable.ic_unliked)
            }
        }

        holder.itemView.setOnClickListener { // setting on click listener
            // for our items of recycler items.
            val product = products[position]
            Toast.makeText(ctx, "Clicked product is " + product.name, Toast.LENGTH_SHORT).show()
            val intent = Intent(ctx, ActivityProductDetail::class.java)
            val args = Bundle()
            args.putSerializable("productDetails", product as Serializable)
            intent.putExtra("intentProductDetails", args)

            ctx.startActivity(intent)
        }
    }

    override fun getItemCount() = products.size

    private fun addOrRemoveInFavs(product : Product, holder : ProductViewHolder): Boolean {
        var inFavs = false
        for (item in favList) {
            if (item.id == product.id) {
                inFavs = true
            }
        }
        if (!inFavs) {
            val dbLike = holder.db.getReference("likes")
            product.parent_cat_id = parentCatId
            dbLike.child(holder.userEmail).child(product.id.toString()).setValue(product)
            favList.add(product)
            inFavs = true
            println("product was added to favs: $product")
        } else {
            val dbLike = holder.db.getReference("likes")
            dbLike.child(holder.userEmail).child(product.id.toString()).removeValue()
            val newFavList = arrayListOf<Product>()
            for (item in favList) {
                if (item.id != product.id) {
                    newFavList.add(item)
                }
            }
            favList = newFavList
            inFavs = false
            println("product was removed from favs: $product")
        }

        return inFavs
    }

    private fun addOrRemoveInBucket(product : Product, holder : ProductViewHolder): Boolean {
        var inBucket = false
        for (item in inBucketList) {
            if (item.id == product.id) {
                inBucket = true
            }
        }
        val result = true

        if (!inBucket) {
            val dbLike = holder.db.getReference("bucket_items")
            product.parent_cat_id = parentCatId
            dbLike.child(holder.userEmail).child(product.id.toString()).setValue(product)
            inBucketList.add(product)
            println("product was added to bucket: $product")
        } else {
            val dbLike = holder.db.getReference("bucket_items")
            dbLike.child(holder.userEmail).child(product.id.toString()).removeValue()
            val newBucketItemList = arrayListOf<Product>()
            for (item in inBucketList) {
                if (item.id != product.id) {
                    newBucketItemList.add(item)
                }
            }
            inBucketList = newBucketItemList
            println("product was removed from bucket: $product")
        }

        return result
    }

}