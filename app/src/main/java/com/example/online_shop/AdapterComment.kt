package com.example.online_shop

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterComment(private val comments: ArrayList<Comment>,
                     private val ctx: Context,
) : RecyclerView.Adapter<AdapterComment.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentAuthor: TextView = itemView.findViewById(R.id.idCommentAuthorTV)
        val commentDate: TextView = itemView.findViewById(R.id.idCommentDateTV)
        val commentText: TextView = itemView.findViewById(R.id.idCommentTextTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_layout, parent, false)
        return CommentViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.commentAuthor.text = comments[position].author
        holder.commentDate.text = comments[position].created_date
        holder.commentText.text = comments[position].text
    }

    override fun getItemCount() = comments.size
}