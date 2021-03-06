package com.school.kt.imagefilters.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.school.kt.imagefilters.data.Image

class ImageAdapter(private val clickListener: (View, Image) -> Unit) : RecyclerView.Adapter<ImageViewHolder>() {

    var images: List<Image> = emptyList()

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = holder.bind(images[position], clickListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        with(SquareImageView(parent.context)) {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            ImageViewHolder(this)
        }

    override fun getItemCount() = images.size
}