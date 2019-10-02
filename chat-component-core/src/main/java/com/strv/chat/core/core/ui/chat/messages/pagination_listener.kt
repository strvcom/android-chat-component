package com.strv.chat.core.core.ui.chat.messages

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(
    val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true

    private val visibleThreshold = 15

    private val visibleItemCount
        get() = layoutManager.childCount

    private val totalItemCount
        get() = layoutManager.itemCount

    private val firstVisibleItemPosition
        get() = layoutManager.findFirstVisibleItemPosition()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
            loadMoreItems(totalItemCount - 1)

            loading = true
        }
    }

    protected abstract fun loadMoreItems(offset: Int)

}