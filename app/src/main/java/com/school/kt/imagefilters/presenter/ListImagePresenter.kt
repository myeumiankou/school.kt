package com.school.kt.imagefilters.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.repository.Result
import com.school.kt.imagefilters.view.ListImageView
import kotlinx.coroutines.*

@InjectViewState
class ListImagePresenter(
    private val repository: ListImageRepository
) : MvpPresenter<ListImageView>() {

    private var job = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onFirstViewAttach() {
        uiScope.launch {
            searchImagesInternal()
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    fun searchImages(query: String?) {
        uiScope.launch {
            searchImagesInternal(query)
        }
    }

    private suspend fun searchImagesInternal(query: String? = null) {
        // UI
        viewState.showProgress()
        val task = GlobalScope.async(Dispatchers.IO) {
            if (query != null) {
                repository.searchImages(query)
            } else {
                repository.latestImages()
            }
        }
        // BG
        val results = task.await()
        // UI
        displayResults(results)
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