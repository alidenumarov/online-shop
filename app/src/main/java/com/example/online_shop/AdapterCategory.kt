package com.example.online_shop

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
import com.squareup.picasso.Picasso
import java.io.Serializable


class AdapterCategory(
    // on below line we are passing variables as category list and context
    private val categoryList: ArrayList<Category>,
    private val context: Context,
) : RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): CategoryViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.category_item,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterCategory.CategoryViewHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        val categoryName = categoryList[position].name
        holder.categoryNameTV.text = categoryName

        val imageUrl = categoryList[position].image_url
        Picasso.get().load(imageUrl).into(holder.categoryIV)

        val products = categoryList[position].products

        holder.itemView.setOnClickListener { // setting on click listener
            // for our items of recycler items.
            Toast.makeText(context, "Clicked category is " + categoryName, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ActivityProduct::class.java)
            val args = Bundle()
            if (products != null) {
                args.putSerializable("products", products as Serializable)
                args.putSerializable("intentParentCategory", categoryList[position].id)
                intent.putExtra("intentProducts", args)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // on below line we are returning our size of our list
        return categoryList.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our category name text view and our image view.
        val categoryNameTV: TextView = itemView.findViewById(R.id.tvName)
        val categoryIV: ImageView = itemView.findViewById(R.id.imgUrl)
    }
}