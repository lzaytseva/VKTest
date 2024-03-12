package com.github.lzaytseva.vktest.data.network

import com.github.lzaytseva.vktest.data.network.dto.ProductDto
import com.github.lzaytseva.vktest.data.network.dto.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyJsonApiService {

    @GET("/products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponse

    @GET("/products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: Int
    ): Response<ProductDto>
}