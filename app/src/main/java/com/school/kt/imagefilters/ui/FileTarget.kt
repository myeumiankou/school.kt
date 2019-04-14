package com.school.kt.imagefilters.ui

import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.FileOutputStream
import java.io.IOException

class FileTarget(
    private var fileName: String,
    private var format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    private var quality: Int = 70
) : SimpleTarget<Bitmap>() {

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        try {
            val out = FileOutputStream(fileName)
            resource.compress(format, quality, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}