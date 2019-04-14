package com.school.kt.imagefilters.view

import android.graphics.drawable.Drawable
import com.arellomobile.mvp.MvpView
import java.io.File

interface EditImageView : MvpView {

    fun showMessage(message : String)

    fun showToastMessage(message : String)

    fun setImage(resource : Drawable?)

    fun shareImage(file: File)

    fun notifyGalleryAboutNewImage(file: File)
}