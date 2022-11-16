package com.example.online_shop

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude

data class Favourite(
    var id: String? = "",
    var name: String? = "",
    var price: Int? = 0,
    var in_favs: Int? = 0,
    var parent_cat_id: String? = "",
    var image_url: String? = null
) :Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "in_favs" to in_favs,
            "parent_cat_id" to parent_cat_id,
            "image_url" to image_url,
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(price)
        parcel.writeValue(in_favs)
        parcel.writeString(parent_cat_id)
        parcel.writeString(image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Favourite> {
        override fun createFromParcel(parcel: Parcel): Favourite {
            return Favourite(parcel)
        }

        override fun newArray(size: Int): Array<Favourite?> {
            return arrayOfNulls(size)
        }
    }


}