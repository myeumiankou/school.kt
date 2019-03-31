package com.school.kt.imagefilters.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import com.school.kt.imagefilters.data.Image
import jp.wasabeef.glide.transformations.BitmapTransformation
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.*

class FilterImageAdapter(
    private val image: Image,
    private val clickListener: ImageClickListener
) : RecyclerView.Adapter<FilterImageViewHolder>() {

    private val filters = listOf(
        ToonFilterTransformation(),
        BlurTransformation(),
        SepiaFilterTransformation(),
        InvertFilterTransformation(),
        PixelationFilterTransformation(),
        SketchFilterTransformation(),
        SwirlFilterTransformation(),
        KuwaharaFilterTransformation(),
        VignetteFilterTransformation(),
        BrightnessFilterTransformation(),
        ContrastFilterTransformation()
    )

    interface ImageClickListener {
        fun onImageClicked(image: Image, transformation: BitmapTransformation)
    }

    override fun onBindViewHolder(holder: FilterImageViewHolder, position: Int) =
        holder.bind(image, filters[position], clickListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterImageViewHolder =
        with(ImageView(parent.context)) {
            layoutParams = LayoutParams(350, 350)  //todo move to dimen
            scaleType = ImageView.ScaleType.CENTER_CROP
            FilterImageViewHolder(this)
        }

    override fun getItemCount() = filters.size
}