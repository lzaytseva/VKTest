package com.github.lzaytseva.vktest.data.network

import android.content.Context
import com.github.lzaytseva.vktest.data.network.dto.ProductDetailsRequest
import com.github.lzaytseva.vktest.data.network.dto.ProductDetailsResponse
import com.github.lzaytseva.vktest.data.network.dto.ProductsRequest
import com.github.lzaytseva.vktest.data.network.dto.Response
import com.github.lzaytseva.vktest.util.ConnectionChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitNetworkClient @Inject constructor(
    private val context: Context,
    private val apiService: DummyJsonApiService
) : NetworkClient {
    override suspend fun doRequest(request: Any): Response {
        if (!ConnectionChecker.isConnected(context)) {
            return Response().apply { resultCode = CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            when (request) {
                is ProductsRequest -> getAllProducts(request.limit, request.skip)
                is ProductDetailsRequest -> getProductDetails(request.id)
                else -> Response().apply { resultCode = CODE_WRONG_REQUEST }
            }
        }
    }

    private suspend fun getAllProducts(limit: Int, skip: Int): Response {
        return try {
            val response = apiService.getProducts(limit, skip)
            response.apply { resultCode = CODE_SUCCESS }
        } catch (t: Throwable) {
            Response().apply { resultCode = CODE_FAILURE }
        }
    }

    private suspend fun getProductDetails(id: Int): Response {
        return try {
            val response = apiService.getProductDetails(id)
            if (response.code() == CODE_SUCCESS && response.body() != null) {
                ProductDetailsResponse(response.body()!!).apply { resultCode = CODE_SUCCESS }
            } else {
                Response().apply { resultCode = CODE_FAILURE }
            }
        } catch (t: Throwable) {
            Response().apply { resultCode = CODE_FAILURE }
        }
    }

    companion object {
        const val CODE_NO_INTERNET = -1
        const val CODE_SUCCESS = 200
        const val CODE_WRONG_REQUEST = 400
        const val CODE_FAILURE = 500
    }
}