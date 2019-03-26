package com.school.kt.imagefilters.view

import com.arellomobile.mvp.MvpView
import com.school.kt.imagefilters.data.Image

interface ListImageView : MvpView {

    fun showResultCount(count : Int)

    fun showListImages(images: List<Image>)

    fun showProgress()

    fun hideProgress()

    fun showNoResultView()

    fun showFail(error : Int)

    fun showError(error : String?)
}