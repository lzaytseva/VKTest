package com.github.lzaytseva.vktest.data.network

import com.github.lzaytseva.vktest.data.network.dto.Response

interface NetworkClient {
    suspend fun doRequest(request: Any): Response
}