package com.school.kt.imagefilters

import android.app.Application
import com.school.kt.imagefilters.di.appModule
import com.school.kt.imagefilters.di.notificationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationWrapper : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ApplicationWrapper)
            modules(appModule, notificationModule)
        }
    }
}