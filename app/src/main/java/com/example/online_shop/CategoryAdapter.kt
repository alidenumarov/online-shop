package com.example.online_shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class CategoryAdapter(
    // on below line we are passing variables as course list and context
    private val categoryList: ArrayList<Category>,
    private val context: Context,
) : RecyclerView.Adapter<CategoryAdapter.CourseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CourseViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.category_item,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CourseViewHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        val categoryName = categoryList.get(position).name

        holder.courseNameTV.text = categoryName

        val imageUrl = categoryList.get(position).image_url
        Picasso.get().load(imageUrl).into(holder.courseIV)

        holder.itemView.setOnClickListener { // setting on click listener
            // for our items of recycler items.
            Toast.makeText(context, "Clicked category is " + categoryName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        // on below line we are returning our size of our list
        return categoryList.size
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our course name text view and our image view.
        val courseNameTV: TextView = itemView.findViewById(R.id.tvName)
        val courseIV: ImageView = itemView.findViewById(R.id.imgUrl)
    }
}