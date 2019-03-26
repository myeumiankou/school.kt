package com.school.kt.imagefilters.di

import android.os.Handler
import com.school.kt.imagefilters.di.ServiceProperties.BASE_URL
import com.school.kt.imagefilters.repository.ImageService
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.repository.ListImageRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { createWebService<ImageService>(BASE_URL) }
    single<ListImageRepository> { ListImageRepositoryImpl(get()) }
    single { Handler() }
}

object ServiceProperties {

    const val BASE_URL = "http://www.splashbase.co/api/v1/images/"
}

inline fun <reified T> createWebService(url: String): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(T::class.java)
}