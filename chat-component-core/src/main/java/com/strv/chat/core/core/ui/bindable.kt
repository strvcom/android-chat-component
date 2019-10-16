package com.strv.chat.core.core.ui

import com.strv.chat.core.core.ui.extensions.OnClickAction

interface Bindable<T> {

    fun bind(item: T, onClickAction: OnClickAction<T>? = null)
}