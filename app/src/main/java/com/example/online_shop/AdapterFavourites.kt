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


class AdapterFavourites(private val favs: ArrayList<Favourite>) : RecyclerView
.Adapter<AdapterFavourites.FavouritesViewHolder>() {
    class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favName: TextView = itemView.findViewById(R.id.tvFavName)
        val favPrice: TextView = itemView.findViewById(R.id.tvFavPrice)
        val favIV: ImageView = itemView.findViewById(R.id.idFavImgUrl)
        val likeFavBtn : Button = itemView.findViewById(R.id.btnFavLike)
        lateinit var db : FirebaseDatabase
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_layout, parent, false)
        return FavouritesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.favName.text = favs[position].name
        holder.favPrice.text = favs[position].price.toString() + " ₸"

        if (favs[position].in_favs == 1) {
            holder.likeFavBtn.text = "Dislike"
        } else {
            holder.likeFavBtn.text = "Like"
        }

        var imageUrl = favs[position].image_url
        if (imageUrl == "") {
            imageUrl = "https://resources.cdn-kaspi.kz/shop/medias/sys_master/images/images/h65/h0f/33125684084766/apple-macbook-air-2020-13-3-mgn63-seryj-100797845-1-Container.jpg"
        }
        Picasso.get().load(imageUrl).into(holder.favIV)

        holder.likeFavBtn.setOnClickListener {
            holder.db = FirebaseDatabase.getInstance()
            val product = favs[position]
            val res = addToBucket(product, holder.db)
            if (res) {
                holder.likeFavBtn.text = "Like"
            } else {
                holder.likeFavBtn.text = "Dislike"
            }
        }
    }

    override fun getItemCount() = favs.size

    private fun addToBucket(fav : Favourite, dbRef : FirebaseDatabase): Boolean {
        var result = true
        if (fav.in_favs == 0) {
            val dbLike = dbRef.getReference("likes")
            fav.in_favs = 1
            dbLike.child(fav.id.toString()).setValue(fav)

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(fav.parent_cat_id.toString()).child("products").
            child(fav.id.toString()).child("in_favs").setValue(1).
            addOnSuccessListener {
                println("Fav was added to bucket: $fav")
                return@addOnSuccessListener
            }
                .addOnFailureListener {
                    result = false
                }
        } else {
            val dbLike = dbRef.getReference("likes")
            dbLike.child(fav.id.toString()).removeValue()

            val dbCategories = dbRef.getReference("categories")

            dbCategories.child(fav.parent_cat_id.toString()).child("products").
            child(fav.id.toString()).child("in_favs").setValue(0).
            addOnSuccessListener {
                println("Fav was removed from bucket: $fav")
            }
                .addOnFailureListener {
                    result = false
                }

        }

        return result

    }
}