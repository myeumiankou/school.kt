package com.school.kt.imagefilters.fragment

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.di.GlideOptions
import com.school.kt.imagefilters.ui.FilterImageAdapter
import com.school.kt.imagefilters.ui.GridItemDecoration
import jp.wasabeef.glide.transformations.BitmapTransformation
import kotlinx.android.synthetic.main.edit_image_fragment_layout.*

// todo add MVP
class EditImageFragment : MvpAppCompatFragment(), FilterImageAdapter.ImageClickListener {

    companion object {
        private const val IMAGE_URL_EXTRA = "image_url"

        fun newInstance(image: Image): EditImageFragment {
            with(Bundle()) {
                putParcelable(IMAGE_URL_EXTRA, image)

                val fragment = EditImageFragment()
                fragment.arguments = this
                return fragment
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.edit_image_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getParcelable<Image>(IMAGE_URL_EXTRA)?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.transitionName = this.url
            }
            loadImage(this)

            with(recyclerView) {
                adapter = FilterImageAdapter(this@apply, this@EditImageFragment)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
            }
        }
    }

    override fun onImageClicked(image: Image, transformation: BitmapTransformation) = loadImage(image, transformation)

    private fun loadImage(image: Image, transformation: BitmapTransformation? = null) {
        val highQualityCallback = object : RequestListener<Drawable> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                textView.text = "High quality image loaded"
                return false
            }
        }


        val lowQualityCallback = object : RequestListener<Drawable> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // TODO find better solution???
                textView.text = "Low quality image loaded (Loading high quality...)"
                Handler().postDelayed({
                    if (transformation != null) {
                        Glide.with(this@EditImageFragment)
                            .load(image.largeUrl)
                            .placeholder(resource)
                            .apply(GlideOptions.bitmapTransform(transformation))
                            .listener(highQualityCallback)
                            .into(imageView)
                    } else {
                        Glide.with(this@EditImageFragment)
                            .load(image.largeUrl)
                            .placeholder(resource)
                            .listener(highQualityCallback)
                            .into(imageView)
                    }
                }, 100)
                return false
            }
        }

        // TODO find better solution???
        if (transformation != null) {
            Glide.with(this).load(image.url).placeholder(imageView.drawable)
                .apply(GlideOptions.bitmapTransform(transformation)).listener(lowQualityCallback).into(imageView)
        } else {
            Glide.with(this).load(image.url).listener(lowQualityCallback).into(imageView)
        }
    }
}