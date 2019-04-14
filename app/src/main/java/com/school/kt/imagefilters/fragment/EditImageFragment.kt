package com.school.kt.imagefilters.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.school.kt.imagefilters.BuildConfig
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.presenter.EditImagePresenter
import com.school.kt.imagefilters.ui.FilterImageAdapter
import com.school.kt.imagefilters.view.EditImageView
import jp.wasabeef.glide.transformations.BitmapTransformation
import kotlinx.android.synthetic.main.edit_image_fragment_layout.*
import java.io.File


class EditImageFragment : MvpAppCompatFragment(), FilterImageAdapter.ImageClickListener, EditImageView {

    companion object {
        private const val IMAGE_URL_EXTRA = "image_url"

        private const val PERMISSION_REQUEST_CODE = 1

        fun newInstance(image: Image): EditImageFragment {
            with(Bundle()) {
                putParcelable(IMAGE_URL_EXTRA, image)

                val fragment = EditImageFragment()
                fragment.arguments = this
                return fragment
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: EditImagePresenter

    @ProvidePresenter
    fun providePresenter(): EditImagePresenter = EditImagePresenter(
        Glide.with(this),
        getImage()!!,
        Handler(),
        Environment.getExternalStorageDirectory()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.edit_image_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        getImage()?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.transitionName = this.url
            }

            with(recyclerView) {
                adapter = FilterImageAdapter(this@apply, this@EditImageFragment)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
            }
        }
    }

    private fun getImage() = arguments?.getParcelable<Image>(IMAGE_URL_EXTRA)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) =
        inflater!!.inflate(R.menu.save_and_share_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save -> {
                if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    presenter.saveImage()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                }
                return true
            }
            R.id.share -> {
                presenter.shareImage()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onImageClicked(image: Image, transformation: BitmapTransformation) {
        presenter.applyTransformation(transformation)
    }

    override fun showMessage(message: String) {
        textView.text = message
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun shareImage(file: File) = with(Intent(Intent.ACTION_SEND)) {
        type = "image/jpeg"
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        val uri = FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID, file)
        putExtra(Intent.EXTRA_STREAM, uri)

        val pm = activity!!.packageManager
        if (resolveActivity(pm) != null) {
            startActivity(this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.saveImage()
            } else {
                showToastMessage("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission")
            }
        }
    }

    override fun notifyGalleryAboutNewImage(file: File) {
        MediaScannerConnection.scanFile(context, arrayOf(file.toString()), arrayOf("image/jpg"),null)
    }

    override fun setImage(resource: Drawable?) = imageView.setImageDrawable(resource)
}