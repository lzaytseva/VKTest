package com.github.lzaytseva.vktest.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lzaytseva.vktest.domain.repository.ProductsRepository
import com.github.lzaytseva.vktest.presentation.state.ProductDetailsScreenState
import com.github.lzaytseva.vktest.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(private val repository: ProductsRepository) :
    ViewModel() {
    private val _screenState =
        MutableLiveData<ProductDetailsScreenState>(ProductDetailsScreenState.Loading)
    val screenState: LiveData<ProductDetailsScreenState>
        get() = _screenState


    fun loadDetails(id: Int) {
        _screenState.value = ProductDetailsScreenState.Loading
        viewModelScope.launch {
            repository.loadProductDetails(id).collect {
                when (it) {
                    is Resource.Success -> {
                        _screenState.value =
                            ProductDetailsScreenState.Content(it.data!!)
                    }

                    is Resource.Error -> {
                        _screenState.value = ProductDetailsScreenState.Error(it.errorType!!)
                    }
                }
            }
        }
    }

}