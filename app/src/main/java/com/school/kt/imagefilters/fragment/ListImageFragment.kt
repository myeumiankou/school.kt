package com.school.kt.imagefilters.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.school.kt.imagefilters.R
import com.school.kt.imagefilters.data.Image
import com.school.kt.imagefilters.presenter.ListImagePresenter
import com.school.kt.imagefilters.view.ListImageView


class ListImageFragment : MvpAppCompatFragment(), ListImageView, SearchView.OnQueryTextListener {

    @InjectPresenter
    lateinit var presenter: ListImagePresenter

    private var messageView: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list_image_fragment_layout, container, true)
        messageView = view.findViewById(R.id.messageView)
        recyclerView = view.findViewById(R.id.recyclerView)
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
        messageView?.setText(images.toString())
    }

    override fun showProgress(show: Boolean) {
        messageView?.setText("Loading...")
    }

    override fun showNoResultView() {
        messageView?.setText("No results. Try another search!")
    }
}