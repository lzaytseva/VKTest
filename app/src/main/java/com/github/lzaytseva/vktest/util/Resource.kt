package com.github.lzaytseva.vktest.util

sealed class Resource<T>(val data: T? = null, val errorType: ErrorType? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorType: ErrorType, data: T? = null, val isLoadingNextPage: Boolean = false) :
        Resource<T>(data, errorType)
}
