package com.school.kt.imagefilters.router

import android.os.Build
import android.support.v4.app.FragmentManager
import android.view.View
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.fragment.EditImageFragment
import com.school.kt.imagefilters.fragment.ListImageFragment
import com.school.kt.imagefilters.ui.applyTransition


class ImagePreviewRouter(private val fm: FragmentManager) {

    fun showImagePreview(fragment: ListImageFragment, imageView: View, image: Image) {
        val editFragment = EditImageFragment.newInstance(image)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.applyTransition()
            editFragment.applyTransition()
        }

        val tag = EditImageFragment::class.toString()
        fm.beginTransaction()
            .addSharedElement(imageView, image.url)
            .addToBackStack(tag)
            .replace(R.id.container, editFragment, tag)
            .commit()
    }
}