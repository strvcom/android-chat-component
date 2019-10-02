package com.strv.chat.library.core.ui.extensions

import android.content.res.ColorStateList
import android.widget.ImageButton
import androidx.core.widget.ImageViewCompat

internal fun ImageButton.tint(colorStateList: ColorStateList) {
    ImageViewCompat.setImageTintList(this, colorStateList)
}