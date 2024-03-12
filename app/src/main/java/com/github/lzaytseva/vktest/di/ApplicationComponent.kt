package com.github.lzaytseva.vktest.di

import android.content.Context
import com.github.lzaytseva.vktest.presentation.ui.ProductDetailsFragment
import com.github.lzaytseva.vktest.presentation.ui.ProductsFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {
    fun inject(productsFragment: ProductsFragment)
    fun inject(productsFragment: ProductDetailsFragment)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}