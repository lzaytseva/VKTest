package com.github.lzaytseva.vktest.app

import android.app.Application
import com.github.lzaytseva.vktest.di.ApplicationComponent
import com.github.lzaytseva.vktest.di.DaggerApplicationComponent


class App: Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            context = this
        )
    }
}

