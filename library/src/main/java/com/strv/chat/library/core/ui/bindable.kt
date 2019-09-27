package com.strv.chat.library.core.ui

import com.strv.chat.library.core.ui.extensions.OnClickAction

interface Bindable<T> {

    fun bind(item: T, onClickAction: OnClickAction<T>)
}