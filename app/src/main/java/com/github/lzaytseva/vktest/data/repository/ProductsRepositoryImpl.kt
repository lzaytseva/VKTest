package com.github.lzaytseva.vktest.data.repository

import com.github.lzaytseva.vktest.data.mapper.ProductMapper
import com.github.lzaytseva.vktest.data.network.NetworkClient
import com.github.lzaytseva.vktest.data.network.RetrofitNetworkClient
import com.github.lzaytseva.vktest.data.network.dto.ProductDetailsRequest
import com.github.lzaytseva.vktest.data.network.dto.ProductDetailsResponse
import com.github.lzaytseva.vktest.data.network.dto.ProductsRequest
import com.github.lzaytseva.vktest.data.network.dto.ProductsResponse
import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.domain.repository.ProductsRepository
import com.github.lzaytseva.vktest.util.ErrorType
import com.github.lzaytseva.vktest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient,
    private val mapper: ProductMapper
) : ProductsRepository {

    private val _products = mutableListOf<Product>()
    private val products: List<Product>
        get() = _products.toList()

    private var skip: Int = 0
    private var total: Int = 0

    private val allProductsWasShown: Boolean
        get() = total == skip
    private val isLoadingNextPage: Boolean
        get() = skip > 0

    override fun loadProducts(): Flow<Resource<List<Product>>> = flow {
        if (allProductsWasShown && products.isNotEmpty()) {
            emit(Resource.Error(ErrorType.NO_MORE_CONTENT))
        }

        val response = networkClient.doRequest(ProductsRequest(skip = skip))
        when (response.resultCode) {

            RetrofitNetworkClient.CODE_SUCCESS -> {
                val productsResponse = response as ProductsResponse
                total = productsResponse.total
                skip += productsResponse.limit
                val loadedProducts = response.products.map { mapper.mapDtoToDomain(it) }
                _products.addAll(loadedProducts)
                emit(Resource.Success(products))
            }

            RetrofitNetworkClient.CODE_FAILURE -> {
                emit(
                    Resource.Error(
                        errorType = ErrorType.SERVER_ERROR,
                        isLoadingNextPage = isLoadingNextPage
                    )
                )
            }

            RetrofitNetworkClient.CODE_NO_INTERNET -> {
                emit(
                    Resource.Error(
                        errorType = ErrorType.NO_INTERNET,
                        isLoadingNextPage = isLoadingNextPage
                    )
                )
            }
        }
    }

    override fun loadProductDetails(id: Int): Flow<Resource<Product>> = flow {
        val response = networkClient.doRequest(ProductDetailsRequest(id))
        when (response.resultCode) {

            RetrofitNetworkClient.CODE_SUCCESS -> {
                emit(
                    Resource.Success(
                        mapper.mapDtoToDomain((response as ProductDetailsResponse).product)
                    )
                )
            }

            RetrofitNetworkClient.CODE_FAILURE -> {
                emit(
                    Resource.Error(
                        errorType = ErrorType.SERVER_ERROR
                    )
                )
            }

            RetrofitNetworkClient.CODE_NO_INTERNET -> {
                emit(
                    Resource.Error(
                        errorType = ErrorType.NO_INTERNET
                    )
                )
            }
        }
    }
}