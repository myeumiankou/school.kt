package com.school.kt.imagefilters.di

import android.os.Handler
import com.school.kt.imagefilters.presenter.ListImagePresenter
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.repository.ListImageRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<ListImageRepository> { ListImageRepositoryImpl() }
    single { Handler() }
}