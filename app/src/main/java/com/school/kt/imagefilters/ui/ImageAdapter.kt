package com.school.kt.imagefilters.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.school.kt.imagefilters.data.Image

class ImageAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    var images: List<Image> = emptyList()

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = SquareImageView(parent.context)
        view.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        return ImageViewHolder(view)
    }

    override fun getItemCount() = images.size
}