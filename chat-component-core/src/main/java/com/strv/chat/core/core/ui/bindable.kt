package com.strv.chat.core.core.ui

import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.domain.ImageLoader

/**
 * Describes how to display data into its place within a RecyclerView.
 *
 * @param T Type of data to display.
 */
interface Bindable<T> {

    /**
     * Binds data to UI components.
     *
     * @param item Item to be binded.
     * @param imageLoader Defines a way how to upload picture's urls to ImageViews. Can be null.
     * @param onClickAction Action that is performed after the user clicks on the [item]. Can be null.
     */
    fun bind(item: T, imageLoader: ImageLoader? = null, onClickAction: OnClickAction<T>? = null)
}