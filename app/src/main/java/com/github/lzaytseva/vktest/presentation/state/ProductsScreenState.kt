package com.github.lzaytseva.vktest.presentation.state

import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.util.ErrorType

sealed interface ProductsScreenState {
    data object Loading : ProductsScreenState

    data object LoadingNextData : ProductsScreenState

    data class Error(val error: ErrorType) : ProductsScreenState

    data class ErrorLoadingNextPage(val error: ErrorType, val messageWasShown: Boolean = false) :
        ProductsScreenState

    data class Content(
        val products: List<Product>
    ) : ProductsScreenState

    data object NoMoreContent: ProductsScreenState
}