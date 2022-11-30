package com.example.online_shop
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Product(
    var id: String? = "",
    var name: String? = "",
    var price: Int? = 0,
    var in_favs: Int? = 0,
    var in_bucket: Int? = 0,
    var count_in_bucket: Int? = 1,
    var parent_cat_id: String? = "",
    var image_url: String? = "",
    var comments : Map<String, Comment> ?= null
) : Parcelable, Serializable