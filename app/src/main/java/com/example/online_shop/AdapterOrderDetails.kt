package com.example.online_shop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.io.Serializable

class AdapterOrderDetails(private val products: ArrayList<Product>,
                     var parentCatId: String,
                     private val ctx: Context,
) : RecyclerView.Adapter<AdapterOrderDetails.OrderDetailViewHolder>() {

    class OrderDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tvODProductName)
        val productPrice: TextView = itemView.findViewById(R.id.tvODTotalPrice)
        val productTotalCount: TextView = itemView.findViewById(R.id.tvODTotalCount)
        val productIV: ImageView = itemView.findViewById(R.id.idODProductImgUrl)
        lateinit var db : FirebaseDatabase
        val userEmail = Firebase.auth.currentUser?.email.toString().replace(".", " ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_details_layout, parent, false)
        return OrderDetailViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.productName.text = products[position].name
        val totalPrice = products[position].count_in_bucket?.times(products[position].price!!)
        holder.productPrice.text = "Total Price per product: " + totalPrice.toString() + " ₸"
        holder.productTotalCount.text = "Total Count per product: " + products[position].count_in_bucket.toString()
        var imageUrl = products[position].image_url
        if (imageUrl == "") {
            imageUrl = "https://resources.cdn-kaspi.kz/shop/medias/sys_master/images/images/h65/h0f/33125684084766/apple-macbook-air-2020-13-3-mgn63-seryj-100797845-1-Container.jpg"
        }
        Picasso.get().load(imageUrl).into(holder.productIV)
    }

    override fun getItemCount() = products.size
}