package com.school.kt.imagefilters

import android.app.Application
import com.school.kt.imagefilters.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationWrapper : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ApplicationWrapper)
            modules(appModule)
        }
    }
}