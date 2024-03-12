package com.github.lzaytseva.vktest.di

import android.content.Context
import com.github.lzaytseva.vktest.data.network.DummyJsonApiService
import com.github.lzaytseva.vktest.data.network.NetworkClient
import com.github.lzaytseva.vktest.data.network.RetrofitNetworkClient
import com.github.lzaytseva.vktest.data.repository.ProductsRepositoryImpl
import com.github.lzaytseva.vktest.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindProductsRepository(impl: ProductsRepositoryImpl): ProductsRepository


    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): DummyJsonApiService {
            return Retrofit.Builder()
                .baseUrl("https://dummyjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DummyJsonApiService::class.java)
        }

        @ApplicationScope
        @Provides
        fun provideNetworkClient(context: Context, apiService: DummyJsonApiService): NetworkClient {
            return RetrofitNetworkClient(context, apiService)
        }
    }
}