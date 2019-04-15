package com.school.kt.imagefilters.presenter

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseCoroutinePresenter<View : MvpView> : MvpPresenter<View>() {

    protected var job = Job()

    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    protected val ioScope = CoroutineScope(Dispatchers.IO + job)

    override fun onDestroy() {
        job.cancel()
    }
}