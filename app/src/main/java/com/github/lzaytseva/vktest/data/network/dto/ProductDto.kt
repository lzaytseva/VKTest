package com.github.lzaytseva.vktest.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("brand") val brand: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("discountPercentage") val discountPercentage: Double,
    @SerializedName("id") val id: Int,
    @SerializedName("images") val images: List<String>,
    @SerializedName("price") val price: Int,
    @SerializedName("rating") val rating: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("thumbnail") val thumbnailUrl: String,
    @SerializedName("title") val name: String
)