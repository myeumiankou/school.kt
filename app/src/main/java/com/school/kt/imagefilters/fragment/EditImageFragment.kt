package com.school.kt.imagefilters.fragment

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import kotlinx.android.synthetic.main.edit_image_fragment_layout.*

class EditImageFragment : MvpAppCompatFragment() {

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
        // todo add MVP
        val image = arguments?.getParcelable<Image>(IMAGE_URL_EXTRA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.transitionName = image?.url
        }
        loadImage(image)
    }

    private fun loadImage(image: Image?) {
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
                Toast.makeText(context, "High quality image loaded", Toast.LENGTH_SHORT).show()
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
                Handler().post {
                    Glide.with(this@EditImageFragment)
                        .load(image?.largeUrl)
                        .placeholder(resource)
                        .listener(highQualityCallback)
                        .into(imageView)
                }
                return false
            }
        }
        Glide.with(this).load(image?.url).listener(lowQualityCallback).into(imageView)
    }
}