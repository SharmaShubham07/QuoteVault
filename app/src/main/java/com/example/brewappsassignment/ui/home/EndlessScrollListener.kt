package com.example.brewappsassignment.ui.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {

    private var loading = true
    private var visibleThreshold = 5

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy <= 0) return

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            loading = true
            onLoadMore()
        } else if (loading && lastVisibleItem + visibleThreshold < totalItemCount) {
            loading = false
        }
    }

    abstract fun onLoadMore()
}
