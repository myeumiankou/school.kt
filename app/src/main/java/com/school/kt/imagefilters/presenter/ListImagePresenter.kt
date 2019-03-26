package com.school.kt.imagefilters.presenter

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.repository.Result
import com.school.kt.imagefilters.view.ListImageView

@InjectViewState
class ListImagePresenter(
    private val repository: ListImageRepository,
    private val uiHandler: Handler
) : MvpPresenter<ListImageView>() {

    override fun onFirstViewAttach() {
        viewState.showProgress()
        // todo run it asynchronously using Kotlin Coroutines
        Thread {
            val results = repository.latestImages()

            uiHandler.post {
                displayResults(results)
            }
        }.start()
    }

    fun searchImages(query: String?) {
        viewState.showProgress()
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

    private fun displayResults(result: Result) = with(viewState) {
        when (result) {
            is Result.Fail -> showFail(result.errorCode)
            is Result.Error -> showError(result.description)
            is Result.Success -> {
                if (result.list.isNullOrEmpty()) {
                    showNoResultView()
                } else {
                    showResultCount(result.list.size)
                    showListImages(result.list)
                    hideProgress()
                }
            }
        }
    }
}