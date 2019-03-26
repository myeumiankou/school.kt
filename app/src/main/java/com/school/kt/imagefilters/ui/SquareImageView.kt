package com.school.kt.imagefilters.ui

import android.content.Context
import android.widget.ImageView

class SquareImageView(context: Context?) : ImageView(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
}