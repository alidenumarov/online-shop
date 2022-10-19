package com.example.online_shop

import java.io.Serializable

data class Product(var name : String ?= null, var price : Int ?= null, var image_url : String ?= null) : Serializable
