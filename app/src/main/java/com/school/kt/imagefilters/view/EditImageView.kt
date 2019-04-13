package com.school.kt.imagefilters.view

import android.graphics.drawable.Drawable
import com.arellomobile.mvp.MvpView

interface EditImageView : MvpView {

    fun showMessage(message : String)

    fun setImage(resource : Drawable?)
}