package com.example.online_shop

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Category(var id : String ?= null,
                    var name : String ?= null,
                    var image_url : String ?= null,
                    var products : MutableMap<String, Product> ?= null)
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "image_url" to image_url,
            "products" to products,
        )
    }
}