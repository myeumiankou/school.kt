package com.school.kt.imagefilters.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.SearchView
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
import kotlinx.android.synthetic.main.list_image_fragment_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ListImageFragment : MvpAppCompatFragment(), ListImageView, SearchView.OnQueryTextListener,
    ImageAdapter.ImageClickListener {

    private val notificationManager: NotificationManager by inject()
    private val repository: ListImageRepository by inject()
    private val uiHandler: Handler by inject()
    private val router: ImagePreviewRouter by inject { parametersOf(fragmentManager) }

    @InjectPresenter
    lateinit var presenter: ListImagePresenter

    @ProvidePresenter
    fun providePresenter(): ListImagePresenter {
        return ListImagePresenter(repository, uiHandler)
    }

    companion object {
        const val IMAGE_ROW_COUNT = 4
    }

    private lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.list_image_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(recyclerView) {
            adapter = ImageAdapter(this@ListImageFragment)
            layoutManager = GridLayoutManager(context, IMAGE_ROW_COUNT)
            addItemDecoration(GridItemDecoration(5, IMAGE_ROW_COUNT))
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.search, menu)
        if (!::searchView.isInitialized) {
            searchView = menu?.findItem(R.id.search)!!.actionView as SearchView
        }
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.notification -> notificationManager.showTestNotification()
        }
        return false
    }

    override fun onImageClicked(view : View, image: Image) {
        router.showImagePreview(this, view, image)
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

    override fun showResultCount(count: Int) {
        Toast.makeText(context, "Results: $count", Toast.LENGTH_SHORT).show()
    }

    override fun showFail(error: Int) {
        messageView.text = "Fail. Code = $error"
    }

    override fun showError(error: String?) {
        messageView.text = "Error. Message: $error"
    }
}