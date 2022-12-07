package com.example.online_shop
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Product(
    var id: String? = "",
    var name: String? = "",
    var price: Int? = 0,
//    var in_favs: ArrayList<String>? = arrayListOf(),
    var in_bucket: ArrayList<String>? = arrayListOf(),
    var count_in_bucket: Int? = 1,
    var parent_cat_id: String? = "",
    var image_url: String? = "",
    var comments : Map<String, Comment> ?= null
) : Serializable