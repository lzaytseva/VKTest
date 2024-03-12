package com.github.lzaytseva.vktest.domain.repository

import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun loadProducts(): Flow<Resource<List<Product>>>
    fun loadProductDetails(id: Int): Flow<Resource<Product>>
}