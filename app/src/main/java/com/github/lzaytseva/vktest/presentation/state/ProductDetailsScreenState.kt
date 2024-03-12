package com.github.lzaytseva.vktest.presentation.state

import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.util.ErrorType

sealed interface ProductDetailsScreenState {
    data object Loading : ProductDetailsScreenState
    data class Error(val error: ErrorType) : ProductDetailsScreenState
    data class Content(
        val product: Product
    ) : ProductDetailsScreenState
}