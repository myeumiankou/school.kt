package com.school.kt.imagefilters.ui

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.school.kt.imagefilters.data.Image

class ImageViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {

    fun bind(image: Image, clickListener: ImageAdapter.ImageClickListener) {
        (itemView as ImageView).apply {
            Glide.with(itemView).load(image.url).centerCrop().into(itemView)
            ViewCompat.setTransitionName(itemView, image.url)
            itemView.setOnClickListener { clickListener.onImageClicked(itemView, image) }
        }
    }
}