package com.strv.chat.library.core.ui.extensions

import com.strv.chat.library.core.ui.view.ItemListDialogFragment

fun selector(title: String?, list: Array<String>, setup: ItemListDialogFragment.() -> Unit) =
    ItemListDialogFragment.newInstance(title, list).apply(setup)