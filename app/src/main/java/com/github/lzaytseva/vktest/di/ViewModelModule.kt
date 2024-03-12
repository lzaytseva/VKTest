package com.github.lzaytseva.vktest.di

import androidx.lifecycle.ViewModel
import com.github.lzaytseva.vktest.presentation.viewmodel.ProductDetailsViewModel
import com.github.lzaytseva.vktest.presentation.viewmodel.ProductsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(ProductsViewModel::class)
    @Binds
    fun bindProductsViewModel(viewModel: ProductsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    @Binds
    fun bindProductDetailsViewModel(viewModel: ProductDetailsViewModel): ViewModel

}