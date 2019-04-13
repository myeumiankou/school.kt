package com.school.kt.imagefilters.fragment

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.save_and_share_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save -> {
                return true
            }
            R.id.share -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onImageClicked(image: Image, transformation: BitmapTransformation) {
        textView.text = "Image transformation has started"
        loadImage(image, transformation)
    }

    private fun loadImage(image: Image, transformation: BitmapTransformation? = null) {
        val highQualityCallback = runInGlideCallback {
            textView.text = "High quality image loaded"
        }

        val lowQualityCallback = runInGlideCallback {
            textView.text = "Low quality image loaded (Loading high quality...)"
            Handler().postDelayed({
                loadImageToView(image.largeUrl, it, transformation, highQualityCallback)
            }, 100)
        }

        loadImageToView(image.url, imageView.drawable, transformation, lowQualityCallback)
    }

    private fun loadImageToView(
        url: String,
        placeHolder: Drawable?,
        transformation: BitmapTransformation?,
        callback: RequestListener<Drawable>
    ) = with(Glide.with(this).load(url)) {
        placeholder(placeHolder)
        transformation?.let {
            apply(GlideOptions.bitmapTransform(transformation))
        }
        listener(callback)
        into(imageView)
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