package com.github.lzaytseva.vktest.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.domain.repository.ProductsRepository
import com.github.lzaytseva.vktest.presentation.state.ProductsScreenState
import com.github.lzaytseva.vktest.util.ErrorType
import com.github.lzaytseva.vktest.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val _screenState = MutableLiveData<ProductsScreenState>(ProductsScreenState.Loading)
    val screenState: LiveData<ProductsScreenState>
        get() = _screenState
    private var isNextPageLoading = false

    init {
        loadProducts()
    }

    fun loadProducts() {
        _screenState.value = ProductsScreenState.Loading
        load()

    }

    fun loadNextPage() {
        if (isNextPageLoading) return
        _screenState.value = ProductsScreenState.LoadingNextData
        load()
    }

    private fun load() {
        isNextPageLoading = true
        viewModelScope.launch {
            repository.loadProducts().collect {
                processResult(
                    products = it.data,
                    error = it.errorType,
                    isNextPageLoading = if (it is Resource.Error) it.isLoadingNextPage else false
                )
            }
            isNextPageLoading = false
        }
    }

    private fun processResult(
        products: List<Product>?,
        error: ErrorType?,
        isNextPageLoading: Boolean
    ) {
        when (error) {
            ErrorType.SERVER_ERROR, ErrorType.NO_INTERNET -> {
                _screenState.value = if (isNextPageLoading) {
                    ProductsScreenState.ErrorLoadingNextPage(error = error)
                } else {
                    ProductsScreenState.Error(error = error)
                }
            }

            ErrorType.NO_MORE_CONTENT -> {
                _screenState.value = ProductsScreenState.NoMoreContent
            }

            null -> {
                if (products?.isNotEmpty() == true) {
                    _screenState.value = ProductsScreenState.Content(products)
                }
            }
        }
    }

    fun setFeedbackWasShown(state: ProductsScreenState.ErrorLoadingNextPage) {
        _screenState.value = state.copy(messageWasShown = true)
    }
}