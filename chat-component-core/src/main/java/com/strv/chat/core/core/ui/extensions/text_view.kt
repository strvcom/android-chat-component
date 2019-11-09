package com.strv.chat.core.core.ui.extensions

import android.widget.TextView

/**
 * Sets the text of the [TextView] to ""
 *
 * @receiver [TextView].
 */
internal fun TextView.reset() {
    text = ""
}