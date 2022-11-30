package com.example.online_shop

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class AdapterBucket(private var bucketProducts: ArrayList<Product>) : RecyclerView
.Adapter<AdapterBucket.BucketViewHolder>() {
    class BucketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bucketProductName: TextView = itemView.findViewById(R.id.tvBucketProductName)
        val bucketProductPrice: TextView = itemView.findViewById(R.id.tvBucketProductPrice)
        val bucketProductIV: ImageView = itemView.findViewById(R.id.idBucketProductImgUrl)
        val curCountET: TextView = itemView.findViewById(R.id.idCurCountET)
        val decreaseCountIV: ImageView = itemView.findViewById(R.id.idImgCountMinus)
        val increaseCountIV: ImageView = itemView.findViewById(R.id.idImgCountPlus)

        lateinit var db : FirebaseDatabase
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bucket_layout, parent, false)
        return BucketViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: BucketViewHolder, position: Int) {
        holder.bucketProductName.text = bucketProducts[position].name
        holder.bucketProductPrice.text = bucketProducts[position].price.toString() + " ₸"
        holder.curCountET.text = bucketProducts[position].count_in_bucket.toString()
        holder.db = FirebaseDatabase.getInstance()

        var imageUrl = bucketProducts[position].image_url
        if (imageUrl == "") {
            imageUrl = "https://resources.cdn-kaspi.kz/shop/medias/sys_master/images/images/h65/h0f/33125684084766/apple-macbook-air-2020-13-3-mgn63-seryj-100797845-1-Container.jpg"
        }

        Picasso.get().load(imageUrl).into(holder.bucketProductIV)

        holder.decreaseCountIV.setOnClickListener{
            var curCount = holder.curCountET.text.toString().toInt()
            if (curCount > 1) {
                curCount -= 1
                addOrRemoveCountInBucket(bucketProducts[position], curCount, holder.db)
                holder.curCountET.text = curCount.toString()
            }
        }

        holder.increaseCountIV.setOnClickListener{
            var curCount = holder.curCountET.text.toString().toInt()
            if (curCount < 999) {
                curCount += 1
                addOrRemoveCountInBucket(bucketProducts[position], curCount, holder.db)
                holder.curCountET.text = curCount.toString()
            }
        }

    }

    override fun getItemCount() = bucketProducts.size

    private fun addOrRemoveCountInBucket(product : Product, curCount : Int, dbRef : FirebaseDatabase): Boolean {
        var result = true
        val db = dbRef.getReference("bucket_items")
        db.child(product.id.toString()).child("count_in_bucket").setValue(curCount).
        addOnSuccessListener {
            println("count was changed: $product")
            return@addOnSuccessListener
        }
            .addOnFailureListener {
                result = false
            }

        return result
    }

}