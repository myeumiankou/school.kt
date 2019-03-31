package com.school.kt.imagefilters.di

import android.os.Handler
import android.support.v4.app.FragmentManager
import com.school.kt.imagefilters.di.ServiceProperties.BASE_URL
import com.school.kt.imagefilters.repository.ImageService
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.repository.ListImageRepositoryImpl
import com.school.kt.imagefilters.router.ImagePreviewRouter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { createWebService<ImageService>(BASE_URL) }
    single<ListImageRepository> { ListImageRepositoryImpl(get()) }
    single { Handler() }

    factory { (fm: FragmentManager) -> ImagePreviewRouter(fm) }
}

object ServiceProperties {

    const val BASE_URL = "http://www.splashbase.co/api/v1/images/"
}

inline fun <reified T> createWebService(url: String): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(T::class.java)
}