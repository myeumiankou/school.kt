package com.school.kt.imagefilters.ui

import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.*
import com.school.kt.imagefilters.fragment.EditImageFragment
import com.school.kt.imagefilters.fragment.ListImageFragment


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun ListImageFragment.applyTransition() {
    enterTransition = Fade()
    exitTransition = Fade()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun EditImageFragment.applyTransition() {
    sharedElementEnterTransition = TransitionSet().apply {
        ordering = TransitionSet.ORDERING_TOGETHER
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeClipBounds())
        addTransition(ChangeImageTransform())
    }

    sharedElementReturnTransition = sharedElementEnterTransition
    enterTransition = Fade()
}