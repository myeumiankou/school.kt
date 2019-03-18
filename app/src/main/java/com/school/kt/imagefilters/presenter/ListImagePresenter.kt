package com.school.kt.imagefilters.presenter

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.view.ListImageView

@InjectViewState
class ListImagePresenter : MvpPresenter<ListImageView>() {

    // todo inject me using Koin
    private val repository = ListImageRepository()
    private val uiHandler = Handler()

    override fun onFirstViewAttach() {
        viewState.showProgress(true)
        // todo run it asynchronously using Kotlin Coroutines
        Thread {
            val results = repository.latestImages()

            uiHandler.post {
                displayResults(results)
                //viewState.showProgress(false)
            }
        }.start()
    }

    fun searchImages(query: String?) {
        viewState.showProgress(true)
        // todo run it asynchronously using Kotlin Coroutines
        Thread {
            val result = if (query != null) {
                repository.searchImages(query)
            } else {
                repository.latestImages()
            }

            uiHandler.post {
                displayResults(result)
                //viewState.showProgress(false)
            }
        }.start()
    }

    private fun displayResults(result: List<Image>?) {
        if (result == null || result.isEmpty()) {
            viewState.showNoResultView()
        } else {
            viewState.showListImages(result)
        }
    }
}