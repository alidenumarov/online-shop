package com.example.online_shop

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class MyOrder(
    var id: String? = "",
    var orderNumber: String? = "",
    var status: String? = "",
    var products : ArrayList<Product> ?= null
) : Serializable
