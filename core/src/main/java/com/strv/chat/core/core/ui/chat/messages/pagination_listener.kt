package com.strv.chat.core.core.ui.chat.messages

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal abstract class PaginationListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0

    private val visibleThreshold = 15

    private val visibleItemCount
        get() = layoutManager.childCount

    private val totalItemCount
        get() = layoutManager.itemCount

    private val firstVisibleItemPosition
        get() = layoutManager.findFirstVisibleItemPosition()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy < 0) {

            if (isLoading() && totalItemCount > (previousTotal + 1)) {
                previousTotal = totalItemCount - 1
            }

            if (!isLoading() && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                loadMoreItems(totalItemCount - 1)
            }
        }
    }

    /**
     * Loads more items with the specified offset.
     *
     * @param offset Number of items to skip.
     */
    protected abstract fun loadMoreItems(offset: Int)

    /**
     * Keeps information about the loading state.
     *
     * @return true if more items are loading false otherwise.
     */
    protected abstract fun isLoading(): Boolean

}