package com.github.lzaytseva.vktest.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("limit") val limit: Int,
    @SerializedName("products") val products: List<ProductDto>,
    @SerializedName("skip") val skip: Int,
    @SerializedName("total") val total: Int
): Response()