package com.strv.chat.core.core.ui

import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.domain.ImageLoader

interface Bindable<T> {

    fun bind(item: T, imageLoader: ImageLoader? = null, onClickAction: OnClickAction<T>? = null)
}