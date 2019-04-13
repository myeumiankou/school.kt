package com.school.kt.imagefilters.presenter

import android.graphics.drawable.Drawable
import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.view.EditImageView
import jp.wasabeef.glide.transformations.BitmapTransformation

@InjectViewState
class EditImagePresenter(
    private val glide: RequestManager,
    private val image: Image,
    private val handler: Handler
) :
    MvpPresenter<EditImageView>() {

    private var currentTransformation: BitmapTransformation? = null

    override fun onFirstViewAttach() {
        loadImage(image)
    }

    fun saveImage() {
        TODO()
    }

    fun shareImage() {
        TODO()
    }

    fun applyTransformation(transformation: BitmapTransformation) {
        currentTransformation = transformation

        with(viewState) {
            showMessage("Image transformation has started")
            loadImage(image, transformation)
        }
    }

    private fun loadImage(image: Image, transformation: BitmapTransformation? = null) {
        val highQualityCallback = runInGlideCallback {
            with(viewState) {
                setImage(it)
                showMessage("High quality image loaded")
            }
        }

        val lowQualityCallback = runInGlideCallback {
            with(viewState) {
                setImage(it)
                showMessage("Low quality image loaded (Loading high quality...)")
            }
            handler.postDelayed({
                loadImageToView(image.largeUrl, it, transformation, highQualityCallback)
            }, 100)
        }

        loadImageToView(image.url, null, transformation, lowQualityCallback)
    }

    private fun loadImageToView(
        url: String,
        placeHolder: Drawable?,
        transformation: BitmapTransformation?,
        callback: RequestListener<Drawable>
    ) = with(glide.load(url)) {
        placeholder(placeHolder)
        transformation?.let {
            apply(com.school.kt.imagefilters.di.GlideOptions.bitmapTransform(transformation))
        }
        listener(callback)
        preload()
    }

    private fun runInGlideCallback(callback: (resource: Drawable?) -> Unit): RequestListener<Drawable> =
        object : RequestListener<Drawable> {
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
                callback.invoke(resource)
                return false
            }
        }
}