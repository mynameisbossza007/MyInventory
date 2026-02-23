package com.jiraphat.myinventory

import java.io.Serializable

data class Product(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
) : Serializable
