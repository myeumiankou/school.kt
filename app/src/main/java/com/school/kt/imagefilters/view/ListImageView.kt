package com.school.kt.imagefilters.view

import com.arellomobile.mvp.MvpView
import com.school.kt.imagefilters.data.Image

interface ListImageView : MvpView {

    fun showResultCount(count : Int)

    fun showListImages(images: List<Image>)

    fun showProgress(show: Boolean)

    fun showNoResultView()
}