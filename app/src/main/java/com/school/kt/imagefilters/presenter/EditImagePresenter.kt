package com.school.kt.imagefilters.presenter

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.annotation.RequiresPermission
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.di.FileFolder
import com.school.kt.imagefilters.di.GlideOptions.bitmapTransform
import com.school.kt.imagefilters.view.EditImageView
import jp.wasabeef.glide.transformations.BitmapTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
            saveImage(outFile.path, obtainImageFile(currentTransformation).get())
            viewState.notifyGalleryAboutNewImage(outFile)

            outFile
        }

        Log.e("EditImagePresenter", "notifying UI in " + Thread.currentThread())
        viewState.showToastMessage("saved to $result")
        viewState.showMessage("saved!")
    }

    fun shareImage() = ioScope.launch {
        with(createTempFile(suffix = ".jpg")) {
            saveImage(path, obtainImageFile(currentTransformation).get())
            viewState.shareImage(this)

            withContext(Dispatchers.Main) {
                viewState.showMessage("shared!")
            }
        }
    }

    private fun obtainImageFile(currentTransformation: BitmapTransformation?): FutureTarget<Bitmap> =
        with(glide.asBitmap().load(image.url)) {
            currentTransformation?.let {
                apply(bitmapTransform(currentTransformation))
            }
            submit()
        }

    private fun saveImage(
        fileName: String,
        bitmap: Bitmap,
        quality: Int = 70,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ) {
        try {
            with(FileOutputStream(fileName)) {
                bitmap.compress(format, quality, this)
                flush()
                close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
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