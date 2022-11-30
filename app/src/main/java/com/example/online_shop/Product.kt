package com.example.online_shop

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
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
) : Parcelable, Serializable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "in_favs" to in_favs,
            "in_bucket" to in_bucket,
            "count_in_bucket" to count_in_bucket,
            "parent_cat_id" to parent_cat_id,
            "image_url" to image_url,
            "comments" to comments,
        )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(price)
        parcel.writeValue(in_favs)
        parcel.writeValue(in_bucket)
        parcel.writeValue(count_in_bucket)
        parcel.writeString(parent_cat_id)
        parcel.writeString(image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}