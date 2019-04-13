package com.school.kt.imagefilters.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.view.EditImageView
import jp.wasabeef.glide.transformations.BitmapTransformation

@InjectViewState
class EditImagePresenter(val image: Image) : MvpPresenter<EditImageView>() {

    override fun onFirstViewAttach() {
        viewState.loadImage(image)
    }

    fun saveImage() {
        TODO()
    }

    fun shareImage() {
        TODO()
    }

    fun applyTransformation(transformation: BitmapTransformation) {
        with(viewState) {
            showMessage("Image transformation has started")
            loadImage(image, transformation)
        }
    }
}