package com.school.kt.imagefilters.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.dsl.NotificationManager
import com.school.kt.imagefilters.presenter.ListImagePresenter
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.router.ImagePreviewRouter
import com.school.kt.imagefilters.ui.GridItemDecoration
import com.school.kt.imagefilters.ui.ImageAdapter
import com.school.kt.imagefilters.view.ListImageView
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sp
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ListImageFragment : MvpAppCompatFragment(), ListImageView, SearchView.OnQueryTextListener {

    private val notificationManager: NotificationManager by inject()
    private val repository: ListImageRepository by inject()
    private val router: ImagePreviewRouter by inject { parametersOf(fragmentManager) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageView: TextView

    @InjectPresenter
    lateinit var presenter: ListImagePresenter

    @ProvidePresenter
    fun providePresenter(): ListImagePresenter = ListImagePresenter(repository)

    companion object {
        const val IMAGE_ROW_COUNT = 4
    }

    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        UI {
            frameLayout {
                recyclerView = recyclerView {
                    lparams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    adapter =
                        ImageAdapter { view, image -> router.showImagePreview(this@ListImageFragment, view, image) }
                    layoutManager = GridLayoutManager(context, IMAGE_ROW_COUNT)
                    addItemDecoration(GridItemDecoration(5, IMAGE_ROW_COUNT))
                }

                messageView = textView {
                    gravity = Gravity.CENTER
                    textSize = sp(10).toFloat()
                    textColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    text = getString(R.string.search_message)
                }
            }
        }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.search, menu)
        searchView = menu?.findItem(R.id.search)!!.actionView as SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.notification -> notificationManager.showTestNotification()
        }
        return false
    }

    override fun onQueryTextChange(query: String) = true

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.searchImages(query)
        return true
    }

    override fun showListImages(images: List<Image>) = with(recyclerView.adapter as ImageAdapter) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun showProgress() {
        with(recyclerView.adapter as ImageAdapter) {
            images = emptyList()
            notifyDataSetChanged()
        }
        messageView.text = "Loading..."
    }

    override fun hideProgress() {
        messageView.text = ""
    }

    override fun showNoResultView() {
        with(recyclerView.adapter as ImageAdapter) {
            images = emptyList()
            notifyDataSetChanged()
        }

        messageView.text = "No results. Try another search!"
    }

    override fun showResultCount(count: Int) = Toast.makeText(context, "Results: $count", Toast.LENGTH_SHORT).show()

    override fun showFail(error: Int) {
        messageView.text = "Fail. Code = $error"
    }

    override fun showError(error: String?) {
        messageView.text = "Error. Message: $error"
    }
}