package com.example.test_api1.product

data class ProductItem(
    val category_id: Int,
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val stock: Int,
    val updated_at: String
)