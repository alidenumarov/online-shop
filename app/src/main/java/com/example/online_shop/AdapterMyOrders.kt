package com.example.online_shop

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable


class AdapterMyOrders(
    // on below line we are passing variables as category list and context
    private val myOrdersList: ArrayList<MyOrder>,
    private val context: Context,
) : RecyclerView.Adapter<AdapterMyOrders.MyOrdersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): MyOrdersViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.my_order_item,
            parent, false
        )

        return MyOrdersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val orderNumber = position + 1
        holder.orderNumberTV.text = "Order #: $orderNumber"

        var imageUrl = R.drawable.ic_baseline_done_outline_24

        val orderStatus = myOrdersList[position].status
        var sts = "waiting"
        if (orderStatus != null) {
            sts = orderStatus
        }

        if (sts.lowercase() == "got".lowercase()) {
            imageUrl = R.drawable.ic_baseline_done_outline_24
            holder.orderStatusTV.setTextColor(Color.parseColor("#41fa5d"))
        } else if (sts.lowercase() == "Waiting".lowercase()) {
            imageUrl = R.drawable.ic_waiting_svgrepo_com
            holder.orderStatusTV.setTextColor(Color.parseColor("#fab041"))
        }
        holder.orderStatusTV.text = sts.uppercase()
        holder.orderIV.setImageResource(imageUrl)

        val myOrders = myOrdersList[position].products

        holder.itemView.setOnClickListener { // setting on click listener
            // for our items of recycler items.
            Toast.makeText(context, "Clicked order is " + orderNumber, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ActivityOrderDetails::class.java)
            val args = Bundle()
            if (myOrders != null) {
                args.putSerializable("myOrders", myOrders as Serializable)
                args.putSerializable("intentParentMyOrders", myOrdersList[position].id)
                intent.putExtra("intentMyOrders", args)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // on below line we are returning our size of our list
        return myOrdersList.size
    }

    class MyOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our category name text view and our image view.
        val orderNumberTV: TextView = itemView.findViewById(R.id.tvMyOrderNumber)
        val orderIV: ImageView = itemView.findViewById(R.id.myOrderStatusImg)
        val orderStatusTV: TextView = itemView.findViewById(R.id.tvMyOrderStatus)
    }
}