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
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.di.GlideOptions
import com.school.kt.imagefilters.presenter.EditImagePresenter
import com.school.kt.imagefilters.ui.FilterImageAdapter
import com.school.kt.imagefilters.ui.GridItemDecoration
import com.school.kt.imagefilters.view.EditImageView
import jp.wasabeef.glide.transformations.BitmapTransformation
import kotlinx.android.synthetic.main.edit_image_fragment_layout.*

class EditImageFragment : MvpAppCompatFragment(), FilterImageAdapter.ImageClickListener, EditImageView {

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

    @InjectPresenter
    lateinit var presenter: EditImagePresenter

    @ProvidePresenter
    fun providePresenter(): EditImagePresenter = EditImagePresenter(Glide.with(this), getImage()!!, Handler())

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
                presenter.saveImage()
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

    override fun setImage(resource: Drawable?) = imageView.setImageDrawable(resource)
}