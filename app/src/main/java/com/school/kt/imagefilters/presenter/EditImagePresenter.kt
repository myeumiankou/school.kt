package com.school.kt.imagefilters.presenter

import android.Manifest
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.annotation.RequiresPermission
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.di.FileFolder
import com.school.kt.imagefilters.di.GlideOptions.bitmapTransform
import com.school.kt.imagefilters.ui.FileTarget
import com.school.kt.imagefilters.view.EditImageView
import jp.wasabeef.glide.transformations.BitmapTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@InjectViewState
class EditImagePresenter(
    private val glide: RequestManager,
    private val image: Image,
    private val handler: Handler,
    private val externalFileDir: File,
    private val screenWidth: Int
) :
    BaseCoroutinePresenter<EditImageView>() {

    private var currentTransformation: BitmapTransformation? = null

    override fun onFirstViewAttach() {
        loadImage(image)
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun saveImage() = uiScope.launch {
        Log.e("EditImagePresenter", "notifying UI in " + Thread.currentThread())
        viewState.showMessage("saving image")
        val result = withContext(Dispatchers.IO) {
            Log.e("EditImagePresenter", "saving image in thread " + Thread.currentThread())
            val directory = File(externalFileDir, File.separator + FileFolder.SAVED_IMAGE_FOLDER_NAME + File.separator)
            directory.mkdirs()
            val outFile = createTempFile("IMG", ".jpg", directory)
            obtainImageFile(outFile.absolutePath, currentTransformation)
            viewState.notifyGalleryAboutNewImage(outFile)

            outFile
        }

        Log.e("EditImagePresenter", "notifying UI in " + Thread.currentThread())
        viewState.showToastMessage("saved to $result")
    }

    fun shareImage() = ioScope.launch {
        with(createTempFile(suffix = ".jpg")) {
            obtainImageFile(path, currentTransformation)
            viewState.shareImage(this)
        }
    }

    private fun obtainImageFile(fileName: String, currentTransformation: BitmapTransformation?) {
        with(glide.asBitmap().load(image.url)) {
            currentTransformation?.let {
                apply(bitmapTransform(currentTransformation))
            }
            into(FileTarget(fileName))
        }
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
                loadBitmap(image.largeUrl, transformation, highQualityCallback)
            }, 100)
        }

        loadBitmap(image.url, transformation, lowQualityCallback)
    }

    private fun loadBitmap(
        url: String,
        transformation: BitmapTransformation?,
        callback: RequestListener<Drawable>
    ) = with(glide.load(url)) {
        transformation?.let {
            apply(bitmapTransform(transformation))
        }
        listener(callback)
        preload(screenWidth, screenWidth)
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