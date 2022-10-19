package com.example.online_shop

data class Category(var name : String ?= null, var image_url : String ?= null,
                    var products : ArrayList<Product> ?= null)
