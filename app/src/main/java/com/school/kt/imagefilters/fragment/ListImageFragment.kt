package com.school.kt.imagefilters.fragment

import android.os.Bundle
import android.os.Handler
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
import com.school.kt.imagefilters.presenter.ListImagePresenter
import com.school.kt.imagefilters.repository.ListImageRepository
import com.school.kt.imagefilters.ui.GridItemDecoration
import com.school.kt.imagefilters.ui.ImageAdapter
import com.school.kt.imagefilters.view.ListImageView
import org.koin.android.ext.android.inject


class ListImageFragment : MvpAppCompatFragment(), ListImageView, SearchView.OnQueryTextListener {

    private val repository : ListImageRepository by inject()
    private val uiHandler : Handler by inject()

    @InjectPresenter
    lateinit var presenter: ListImagePresenter

    @ProvidePresenter
    fun providePresenter() : ListImagePresenter {
        return ListImagePresenter(repository, uiHandler)
    }

    companion object {
        const val IMAGE_ROW_COUNT = 4
    }

    private var messageView: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var adapter = ImageAdapter()
    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list_image_fragment_layout, container, true)
        messageView = view.findViewById(R.id.messageView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = GridLayoutManager(context, IMAGE_ROW_COUNT)
        recyclerView?.addItemDecoration(GridItemDecoration(10, IMAGE_ROW_COUNT))
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search, menu)
        val menuItem = menu?.findItem(R.id.search)
        searchView = menuItem!!.actionView as SearchView
        searchView!!.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(query: String): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.searchImages(query)
        return true
    }

    override fun showListImages(images: List<Image>) {
        adapter.images = images
        adapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        adapter.images = emptyList()
        adapter.notifyDataSetChanged()
        messageView?.text = "Loading..."
    }

    override fun hideProgress() {
        messageView?.text = ""
    }

    override fun showNoResultView() {
        adapter.images = emptyList()
        adapter.notifyDataSetChanged()
        messageView?.text = "No results. Try another search!"
    }

    override fun showResultCount(count: Int) {
        Toast.makeText(context, "Results: $count", Toast.LENGTH_SHORT).show()
    }

    override fun showFail(error: Int) {
        messageView?.text = "Fail. Code = $error"
    }

    override fun showError(error: String?) {
        messageView?.text = "Error. Message: $error"
    }
}