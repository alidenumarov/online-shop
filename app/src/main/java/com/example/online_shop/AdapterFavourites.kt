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


class AdapterFavourites(private var favList: ArrayList<Product>,
                        private var inBucketList: ArrayList<Product>,
                        private val ctx: Context) : RecyclerView
.Adapter<AdapterFavourites.FavouritesViewHolder>() {
    class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favName: TextView = itemView.findViewById(R.id.tvFavName)
        val favPrice: TextView = itemView.findViewById(R.id.tvFavPrice)
        val favIV: ImageView = itemView.findViewById(R.id.idFavImgUrl)
        val likeFavBtn : Button = itemView.findViewById(R.id.btnToBucketFromFav)
        val likeFavImg : ImageView = itemView.findViewById(R.id.imgLikeId)
        lateinit var db : FirebaseDatabase
        val userEmail = Firebase.auth.currentUser?.email.toString().replace(".", " ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_layout, parent, false)
        return FavouritesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.likeFavBtn.setOnClickListener {
            val product = favList[position]
            holder.db = FirebaseDatabase.getInstance()
            val res = addOrRemoveInBucket(product, holder)
            if (res) {
                val btnTxt = holder.likeFavBtn.text
                if (btnTxt.toString().uppercase() == "to bucket".uppercase()) {
                    holder.likeFavBtn.text = "remove from bucket".uppercase()
                } else {
                    holder.likeFavBtn.text = "to bucket".uppercase()
                }
            }
        }

        holder.likeFavImg.setOnClickListener {
            val product = favList[position]
            holder.db = FirebaseDatabase.getInstance()
            val res = addOrRemoveInFavs(product, holder)
            if (res) {
                holder.likeFavImg.setImageResource(R.drawable.ic_liked)
            } else {
                holder.likeFavImg.setImageResource(R.drawable.ic_unliked)
            }
        }

        holder.favName.text = favList[position].name
        holder.favPrice.text = favList[position].price.toString() + " ₸"
        holder.likeFavImg.setImageResource(R.drawable.ic_liked)

        var inBuket = false
        for (item in inBucketList) {
            if (item.id == favList[position].id) {
                inBuket = true
                holder.likeFavBtn.text = "remove from bucket".uppercase()
                break
            }
        }
        if (!inBuket) {
            holder.likeFavBtn.text = "to bucket".uppercase()
        }


//        if (favs[position].in_bucket == 1) {
//            holder.likeFavBtn.text = "remove from bucket".uppercase()
//        } else {
//            holder.likeFavBtn.text = "to bucket".uppercase()
//        }
//
//        for (item in favs[position].in_favs!!) {
//        }
//        if (favs[position].in_favs == 1) {
//        } else {
//            holder.likeFavImg.setImageResource(R.drawable.ic_unliked)
//        }

        var imageUrl = favList[position].image_url
        if (imageUrl == "") {
            imageUrl = "https://resources.cdn-kaspi.kz/shop/medias/sys_master/images/images/h65/h0f/33125684084766/apple-macbook-air-2020-13-3-mgn63-seryj-100797845-1-Container.jpg"
        }
        Picasso.get().load(imageUrl).into(holder.favIV)

        holder.itemView.setOnClickListener { // setting on click listener
            // for our items of recycler items.
            val product = favList[position]
            Toast.makeText(ctx, "Clicked product is " + product.name, Toast.LENGTH_SHORT).show()
            val intent = Intent(ctx, ActivityProductDetail::class.java)
            val args = Bundle()
            args.putSerializable("productDetails", product as Serializable)
            intent.putExtra("intentProductDetails", args)

            ctx.startActivity(intent)
        }
    }

    override fun getItemCount() = favList.size

//    private fun addOrRemoveInFavs(fav : Product, favList: ArrayList<Product>, holder : FavouritesViewHolder): Boolean {
//        var inFavs = false
//        for (item in favList) {
//            if (item.id == fav.id) {
//                inFavs = true
//                break
//            }
//        }
//
//        var result = true
//        if (!inFavs) {
//            val dbLike = holder.db.getReference("likes")
//            dbLike.child(holder.userEmail).child(fav.id.toString()).child("in_favs").setValue(fav)
//        } else {
//            val dbLike = holder.db.getReference("likes")
//            dbLike.child(holder.userEmail).child(fav.id.toString()).removeValue()
//        }
//
//        return result
//    }
//
//    private fun addOrRemoveInBucket(product : Product, dbRef : FirebaseDatabase): Boolean {
//        var result = true
////        if (product.in_bucket == 0) {
////            val dbButton = dbRef.getReference("bucket_items")
////            product.in_bucket = 1
////            dbButton.child(product.id.toString()).setValue(product)
////
////            val dbCategories = dbRef.getReference("categories")
////
////            dbCategories.child(product.parent_cat_id.toString()).child("products").
////            child(product.id.toString()).child("in_bucket").setValue(1).
////            addOnSuccessListener {
////                println("product was added to bucket: ${product.id}")
////                return@addOnSuccessListener
////            }
////            .addOnFailureListener {
////                result = false
////            }
////
////            val dbLike = dbRef.getReference("likes")
////            val a = dbLike.child(product.id.toString()).get()
////            a.addOnSuccessListener { it ->
////                if (it.value != null) {
////                    dbLike.child(product.id.toString()).child("in_bucket").setValue(1)
////                }
////            }
////
////        } else {
////            val dbButton = dbRef.getReference("bucket_items")
////            dbButton.child(product.id.toString()).removeValue()
////
////            val dbCategories = dbRef.getReference("categories")
////            dbCategories.child(product.parent_cat_id.toString()).child("products").
////            child(product.id.toString()).child("in_bucket").setValue(0).
////            addOnSuccessListener {
////                println("product was removed from bucket: ${product.id}")
////            }
////                .addOnFailureListener {
////                    result = false
////                }
////
////            val dbLike = dbRef.getReference("likes")
////            val a = dbLike.child(product.id.toString()).get()
////            a.addOnSuccessListener { it ->
////                if (it.value != null) {
////                    dbLike.child(product.id.toString()).child("in_bucket").setValue(0)
////                }
////            }
////        }
//
//        return result
//    }

    private fun addOrRemoveInFavs(product : Product, holder : FavouritesViewHolder): Boolean {
    var inFavs = false
    for (item in favList) {
        if (item.id == product.id) {
            inFavs = true
        }
    }
    if (!inFavs) {
        val dbLike = holder.db.getReference("likes")
//        product.parent_cat_id = parentCatId
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
        inFavs = false
        favList = newFavList
        println("product was removed from favs: $product")
    }

    return inFavs
}

    private fun addOrRemoveInBucket(product : Product, holder : FavouritesViewHolder): Boolean {
        var inBucket = false
        for (item in inBucketList) {
            if (item.id == product.id) {
                inBucket = true
            }
        }
        val result = true

        if (!inBucket) {
            val dbLike = holder.db.getReference("bucket_items")
//            product.parent_cat_id = parentCatId
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