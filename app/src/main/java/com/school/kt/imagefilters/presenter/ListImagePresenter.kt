package com.school.kt.imagefilters.presenter

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.repository.Result
import com.school.kt.imagefilters.view.ListImageView

@InjectViewState
class ListImagePresenter(
    val repository: ListImageRepository,
    val uiHandler: Handler
) : MvpPresenter<ListImageView>() {

    override fun onFirstViewAttach() {
        viewState.showProgress(true)
        // todo run it asynchronously using Kotlin Coroutines
        Thread {
            val results = repository.latestImages()

            uiHandler.post {
                displayResults(results)
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
            }
        }.start()
    }

    private fun displayResults(result: Result) {
        when (result) {
            is Result.Fail -> viewState.showFail(result.errorCode)
            is Result.Error -> viewState.showError(result.description)
            is Result.Success -> {
                if (result.list.isNullOrEmpty()) {
                    viewState.showNoResultView()
                } else {
                    viewState.showResultCount(result.list.size)
                    viewState.showListImages(result.list)
                    viewState.showProgress(false)
                }
            }
        }
    }
}