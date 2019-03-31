package com.school.kt.imagefilters.ui

import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.di.GlideOptions
import jp.wasabeef.glide.transformations.BitmapTransformation

class FilterImageViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {

    fun bind(
        image: Image,
        transformation: BitmapTransformation,
        clickListener: FilterImageAdapter.ImageClickListener
    ) {
        with(itemView as ImageView) {
            Glide.with(this).load(image.url).apply(GlideOptions.bitmapTransform(transformation)).into(this)
            setOnClickListener { clickListener.onImageClicked(image, transformation) }
        }
    }
}