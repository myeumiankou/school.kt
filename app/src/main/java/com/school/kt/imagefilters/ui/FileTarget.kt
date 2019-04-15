package com.school.kt.imagefilters.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.FileOutputStream
import java.io.IOException

class FileTarget(
    private var fileName: String,
    private var format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    private var quality: Int = 70
) : CustomTarget<Bitmap>() {

    override fun onLoadCleared(placeholder: Drawable?) {
        // do nothing
    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        try {
            with(FileOutputStream(fileName)) {
                resource.compress(format, quality, this)
                flush()
                close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}