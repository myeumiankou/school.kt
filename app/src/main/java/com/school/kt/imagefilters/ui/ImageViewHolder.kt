package com.school.kt.imagefilters.ui

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.school.kt.imagefilters.data.Image

class ImageViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {

    fun bind(image: Image, clickListener: (View, Image) -> Unit) {
        with(itemView as ImageView) {
            Glide.with(this).load(image.url).centerCrop().into(this)
            ViewCompat.setTransitionName(this, image.url)
            setOnClickListener { clickListener(itemView, image) }
        }
    }
}