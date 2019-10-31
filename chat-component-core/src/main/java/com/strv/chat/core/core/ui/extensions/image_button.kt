package com.strv.chat.core.core.ui.extensions

import android.content.res.ColorStateList
import android.widget.ImageButton
import androidx.core.widget.ImageViewCompat

/**
 * Applies a tint to the image drawable.
 *
 * @receiver [ImageButton]
 */
internal fun ImageButton.tint(colorStateList: ColorStateList) {
    ImageViewCompat.setImageTintList(this, colorStateList)
}