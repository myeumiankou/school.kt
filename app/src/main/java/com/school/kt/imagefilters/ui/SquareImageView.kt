package com.school.kt.imagefilters.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class SquareImageView : ImageView {

    constructor(ctx: Context) : super(ctx)

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
}