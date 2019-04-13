package com.school.kt.imagefilters.view

import com.arellomobile.mvp.MvpView
import com.school.kt.imagefilters.data.Image
import jp.wasabeef.glide.transformations.BitmapTransformation

interface EditImageView : MvpView {

    fun loadImage(image: Image, transformation: BitmapTransformation? = null)

    fun showMessage(message : String)
}